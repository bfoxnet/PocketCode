package com.bfoxnet.pocketcode.activities.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bfoxnet.pocketcode.App.Companion.themeManager
import com.bfoxnet.pocketcode.activities.main.navigation.MainRouteRegistry
import com.bfoxnet.pocketcode.activities.main.navigation.MainRoutes
import com.bfoxnet.pocketcode.activities.main.session.DocumentStateDatabase
import com.bfoxnet.pocketcode.activities.main.session.SessionManager
import com.bfoxnet.pocketcode.activities.main.ui.DisclaimerScreen
import com.bfoxnet.pocketcode.activities.main.ui.MainContentHost
import com.bfoxnet.pocketcode.commands.KeybindingsManager
import com.bfoxnet.pocketcode.drawer.DrawerPersistence
import com.bfoxnet.pocketcode.drawer.DrawerViewModel
import com.rk.extension.api.IntentHandleRegistry
import com.bfoxnet.pocketcode.file.FileManager
import com.bfoxnet.pocketcode.file.FilePermission
import com.bfoxnet.pocketcode.file.toFileObject
import com.bfoxnet.pocketcode.lsp.LspRegistry
import com.bfoxnet.pocketcode.resources.getFilledString
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.settings.Settings
import com.bfoxnet.pocketcode.settings.support.handleSupport
import com.bfoxnet.pocketcode.tabs.editor.applyHighlightingAndConnectLSP
import com.bfoxnet.pocketcode.utils.errorDialog
import com.bfoxnet.pocketcode.utils.toast
import java.lang.ref.WeakReference
import kotlin.time.Duration.Companion.milliseconds
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

var mainNavController = WeakReference<NavController?>(null)

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: MainViewModel
        private set

    lateinit var drawerViewModel: DrawerViewModel
        private set

    val fileManager = FileManager(this)

    // suspend (isForeground) -> Unit
    val foregroundListener = hashMapOf<Any, suspend (Boolean) -> Unit>()

    companion object {
        var isPaused = false
        private var activityRef = WeakReference<MainActivity?>(null)
        var instance: MainActivity?
            get() = activityRef.get()
            private set(value) {
                activityRef = WeakReference(value)
            }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onPause() {
        isPaused = true
        GlobalScope.launch(Dispatchers.IO) {
            SessionManager.saveSession(viewModel.tabs, viewModel.currentTabIndex)
            DrawerPersistence.saveState(drawerViewModel)
            foregroundListener.values.forEach { it.invoke(false) }

            LspRegistry.updateConfiguration(this@MainActivity)
        }
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        isPaused = false
        instance = this
        lifecycleScope.launch(Dispatchers.IO) {
            handleIntent(intent)
            foregroundListener.values.forEach { it.invoke(true) }

            val lspConfigChanges = LspRegistry.getConfigurationChanges(this@MainActivity)
            if (lspConfigChanges.isNotEmpty()) {
                val affectedExtensions = lspConfigChanges.flatMap { it.supportedExtensions }
                viewModel.editorTabs
                    .filterWithFiles { _, file -> affectedExtensions.contains(file.getExtension()) }
                    .forEach { it.applyHighlightingAndConnectLSP() }
            }

            delay(1000.milliseconds)
            handleSupport()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
    }

    suspend fun handleIntent(intent: Intent) {
        if (Intent.ACTION_VIEW == intent.action || Intent.ACTION_EDIT == intent.action) {
            if (intent.data == null) {
                errorDialog(msg = strings.invalid_intent.getFilledString(intent.toString()))
                return
            }

            val uri = intent.data!!

            if (uri.toString().startsWith("content://telephony")) {
                toast(strings.unsupported_content)
                return
            }

            val file = uri.toFileObject(expectedIsFile = true)

            if (IntentHandleRegistry.handleIntent(file)) {
                setIntent(Intent())
                return
            }

            viewModel.awaitSessionRestoration()
            viewModel.editorManager.openFile(file, projectRoot = null, switchToTab = true)
            setIntent(Intent())
        }
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        val handledEvent = KeybindingsManager.handleGlobalEvent(event, this)
        if (handledEvent) return true
        return super.dispatchKeyEvent(event)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(themeManager.calculateEffectiveNightMode())
        super.onCreate(savedInstanceState)
        instance = this

        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        drawerViewModel = ViewModelProvider(this)[DrawerViewModel::class.java]

        val splash = installSplashScreen()
        splash.setKeepOnScreenCondition { !themeManager.activeThemeReady }

        lifecycleScope.launch {
            themeManager.loadActiveTheme()
            themeManager.indexLocalThemes()
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val db = DocumentStateDatabase.getDatabase(applicationContext)
            db.documentStateDao().deleteOldestRecords(1000)
        }

        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            val navController = rememberNavController()
            mainNavController = WeakReference(navController)
            val startDestination = remember {
                if (Settings.shown_disclaimer) {
                    MainRoutes.Main.route
                } else {
                    MainRoutes.Disclaimer.route
                }
            }
            NavHost(
                navController = navController,
                startDestination = startDestination,
            ) {
                composable(MainRoutes.Main.route) {
                    MainContentHost()
                    LaunchedEffect(Unit) { FilePermission.verifyStoragePermission(this@MainActivity) }
                }
                composable(MainRoutes.Disclaimer.route) { DisclaimerScreen(navController) { finishAffinity() } }

                MainRouteRegistry.routes.value.forEach { customRoute ->
                    composable(customRoute.route, arguments = customRoute.arguments) { backStackEntry ->
                        customRoute.content(navController, backStackEntry)
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        FilePermission.onRequestPermissionsResult(requestCode, grantResults, lifecycleScope, this)
    }
}
