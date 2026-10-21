package com.bfoxnet.pocketcode

import android.app.Application
import android.os.Build
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.pm.PackageInfoCompat
import androidx.core.os.LocaleListCompat
import com.github.anrwatchdog.ANRWatchDog
import com.bfoxnet.pocketcode.activities.main.session.SessionManager
import com.bfoxnet.pocketcode.commands.CommandProvider
import com.bfoxnet.pocketcode.commands.KeybindingsManager
import com.bfoxnet.pocketcode.crashhandler.CrashHandler
import com.bfoxnet.pocketcode.editor.CodeHighlighter
import com.bfoxnet.pocketcode.editor.FontCache
import com.bfoxnet.pocketcode.editor.KeywordManager
import com.bfoxnet.pocketcode.editor.LanguageManager
import com.rk.extension.ActivityProvider
import com.bfoxnet.pocketcode.icons.pack.IconPackManager
import com.bfoxnet.pocketcode.lsp.FileIconProvider
import com.bfoxnet.pocketcode.lsp.LspPersistence
import com.bfoxnet.pocketcode.lsp.MarkdownImageProvider
import com.bfoxnet.pocketcode.resources.Res
import com.bfoxnet.pocketcode.settings.Preference
import com.bfoxnet.pocketcode.settings.Settings
import com.bfoxnet.pocketcode.settings.debugOptions.LogcatService
import com.bfoxnet.pocketcode.settings.debugOptions.startThemeFlipperIfNotRunning
import com.bfoxnet.pocketcode.settings.editor.DEFAULT_APP_FONT_PATH
import com.bfoxnet.pocketcode.settings.editor.DEFAULT_EDITOR_FONT_PATH
import com.bfoxnet.pocketcode.settings.editor.DEFAULT_TERMINAL_FONT_PATH
import com.bfoxnet.pocketcode.theme.ThemeManager
import com.bfoxnet.pocketcode.utils.application
import com.bfoxnet.pocketcode.utils.getTempDir
import com.bfoxnet.pocketcode.utils.logError
import com.bfoxnet.pocketcode.utils.logInfo
import java.util.Locale
import java.util.concurrent.Executors
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@OptIn(DelicateCoroutinesApi::class)
open class App : Application() {
    companion object {
        val versionCode: Long by lazy {
            val app = application ?: throw IllegalStateException("Application is not initialized yet")
            PackageInfoCompat.getLongVersionCode(app.packageManager.getPackageInfo(app.packageName, 0))
        }

        private var _iconPackManager: IconPackManager? = null
        val iconPackManager: IconPackManager
            get() {
                if (_iconPackManager == null) {
                    _iconPackManager = IconPackManager(application!!)
                }

                return _iconPackManager!!
            }

        private var _themeManager: ThemeManager? = null
        val themeManager: ThemeManager
            get() {
                if (_themeManager == null) {
                    _themeManager = ThemeManager()
                }

                return _themeManager!!
            }
    }

    init {
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler)
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate() {
        super.onCreate()
        application = this
        Res.application = this

        logInfo("App.onCreate")
        registerActivityLifecycleCallbacks(ActivityProvider)

        LspPersistence.restoreServers()

        MarkdownImageProvider.register()
        FileIconProvider.register()

        CommandProvider.buildCommands()
        KeybindingsManager.loadKeybindings()

        val currentLocale = Locale.forLanguageTag(Settings.current_lang)
        val appLocale = LocaleListCompat.create(currentLocale)
        AppCompatDelegate.setApplicationLocales(appLocale)

        GlobalScope.launch(Dispatchers.IO) {
            launch(Dispatchers.IO) {
                iconPackManager.indexLocalPacks()
                iconPackManager.indexStoreIconPacks()
            }
            launch(Dispatchers.IO) {
                themeManager.indexLocalThemes()
                themeManager.indexStoreThemes()
            }

            launch { LanguageManager.initGrammarRegistry() }

            launch { KeywordManager.initKeywordRegistry(this@App) }

            launch { CodeHighlighter.registerMarkdownCodeHighlighter(this@App) }

            launch(Dispatchers.IO) { SessionManager.preloadSession() }

            launch(Dispatchers.IO) {
                val editorFontPath = Settings.editor_font_path.ifEmpty { DEFAULT_EDITOR_FONT_PATH }
                val isEditorAsset = if (editorFontPath.isNotEmpty()) Settings.is_editor_font_asset else true

                val appFontPath = Settings.app_font_path.ifEmpty { DEFAULT_APP_FONT_PATH }
                val isAppAsset = if (editorFontPath.isNotEmpty()) Settings.is_app_font_asset else true

                val terminalFontPath = Settings.terminal_font_path.ifEmpty { DEFAULT_TERMINAL_FONT_PATH }
                val isTerminalAsset = if (terminalFontPath.isNotEmpty()) Settings.is_terminal_font_asset else true

                FontCache.loadFont(this@App, editorFontPath, isEditorAsset)
                FontCache.loadFont(this@App, appFontPath, isAppAsset)
                FontCache.loadFont(this@App, terminalFontPath, isTerminalAsset)
            }

            launch(Dispatchers.IO) { Preference.preloadAllSettings() }

            launch { DocumentProvider.setDocumentProviderEnabled(this@App, Settings.expose_home_dir) }

            launch(Dispatchers.IO) {
                getTempDir().apply {
                    if (exists() && listFiles().isNullOrEmpty().not()) {
                        deleteRecursively()
                    }
                }
            }

            launch { runCatching { UpdateChecker.checkForUpdates("main") } }

            // wait until UpdateManager is done, it should only take few milliseconds
            UpdateManager.inspect()

            // debug options
            startThemeFlipperIfNotRunning()
            if (Settings.enable_logcat) {
                LogcatService.start(this@App)
            }
        }

        if (Settings.anr_watchdog) {
            ANRWatchDog().start()
        }

        if (Settings.strict_mode) {
            StrictMode.setVmPolicy(
                StrictMode.VmPolicy.Builder()
                    .apply {
                        detectAll()
                        penaltyLog()
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                            penaltyListener(Executors.newSingleThreadExecutor()) { violation ->
                                logError(violation)
                                violation.cause?.let { throw it }
                            }
                        }
                    }
                    .build()
            )
        }
    }
}
