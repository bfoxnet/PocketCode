package com.bfoxnet.pocketcode.activities.settings

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.bfoxnet.pocketcode.animations.NavigationAnimationTransitions
import com.bfoxnet.pocketcode.lsp.LspRegistry
import com.bfoxnet.pocketcode.settings.SettingsRegistry
import com.bfoxnet.pocketcode.settings.SettingsScreen
import com.bfoxnet.pocketcode.settings.about.AboutScreen
import com.bfoxnet.pocketcode.settings.app.SettingsAppScreen
import com.bfoxnet.pocketcode.settings.debugOptions.AppLogs
import com.bfoxnet.pocketcode.settings.debugOptions.DeveloperOptions
import com.bfoxnet.pocketcode.settings.editor.AppFontScreen
import com.bfoxnet.pocketcode.settings.editor.DefaultEncoding
import com.bfoxnet.pocketcode.settings.editor.DefaultLineEnding
import com.bfoxnet.pocketcode.settings.editor.EditExtraKeys
import com.bfoxnet.pocketcode.settings.editor.EditToolbarActions
import com.bfoxnet.pocketcode.settings.editor.EditorFontScreen
import com.bfoxnet.pocketcode.settings.editor.ExcludeFiles
import com.bfoxnet.pocketcode.settings.editor.FormatterSettings
import com.bfoxnet.pocketcode.settings.editor.SettingsEditorScreen
import com.bfoxnet.pocketcode.settings.keybinds.KeybindingsScreen
import com.bfoxnet.pocketcode.settings.language.LanguageScreen
import com.bfoxnet.pocketcode.settings.lsp.LspInitializationOptions
import com.bfoxnet.pocketcode.settings.lsp.LspServerDetail
import com.bfoxnet.pocketcode.settings.lsp.LspServerLogs
import com.bfoxnet.pocketcode.settings.lsp.LspSettings
import com.bfoxnet.pocketcode.settings.support.Support
import com.bfoxnet.pocketcode.settings.theme.ThemeScreen

@Composable
fun SettingsNavHost(navController: NavHostController, activity: SettingsActivity) {
    NavHost(
        navController = navController,
        startDestination = SettingsRoutes.Settings.route,
        enterTransition = { NavigationAnimationTransitions.enterTransition },
        exitTransition = { NavigationAnimationTransitions.exitTransition },
        popEnterTransition = { NavigationAnimationTransitions.popEnterTransition },
        popExitTransition = { NavigationAnimationTransitions.popExitTransition },
    ) {
        composable(SettingsRoutes.Settings.route) { SettingsScreen(navController) }
        composable(SettingsRoutes.AppSettings.route) { SettingsAppScreen(activity, navController) }
        composable(SettingsRoutes.EditorSettings.route) { SettingsEditorScreen(navController) }
        composable(SettingsRoutes.Keybindings.route) { KeybindingsScreen() }

        composable(SettingsRoutes.About.route) { AboutScreen() }
        composable(SettingsRoutes.EditorFontScreen.route) { EditorFontScreen() }
        composable(SettingsRoutes.AppFontScreen.route) { AppFontScreen() }

        composable(SettingsRoutes.DefaultEncoding.route) { DefaultEncoding() }
        composable(SettingsRoutes.DefaultLineEnding.route) { DefaultLineEnding() }
        composable(SettingsRoutes.ToolbarActions.route) { EditToolbarActions() }
        composable(SettingsRoutes.ExtraKeys.route) { EditExtraKeys() }
        composable(
            "${SettingsRoutes.ExcludeFiles.route}/{isDrawer}",
            arguments = listOf(navArgument("isDrawer", builder = { type = NavType.BoolType })),
        ) {
            val isDrawer = it.arguments?.getBoolean("isDrawer")!!
            ExcludeFiles(isDrawer)
        }
        composable(SettingsRoutes.DeveloperOptions.route) { DeveloperOptions(navController = navController) }
        composable(SettingsRoutes.AppLogs.route) { AppLogs() }
        composable(SettingsRoutes.Support.route) { Support() }
        composable(SettingsRoutes.LanguageScreen.route) { LanguageScreen() }
        composable(SettingsRoutes.Formatters.route) { FormatterSettings(navController) }
        composable(SettingsRoutes.LspSettings.route) { LspSettings(navController) }
        composable(
            "${SettingsRoutes.LspServerDetail.route}/{serverId}",
            arguments = listOf(navArgument("serverId", builder = { type = NavType.StringType })),
        ) { backStackEntry ->
            val serverId = backStackEntry.arguments?.getString("serverId")!!
            val server = LspRegistry.getForId(serverId)!!
            LspServerDetail(navController, server)
        }
        composable(
            "${SettingsRoutes.LspInitializationOptions.route}/{serverId}",
            arguments = listOf(navArgument("serverId", builder = { type = NavType.StringType })),
        ) { backStackEntry ->
            val serverId = backStackEntry.arguments?.getString("serverId")!!
            val server = LspRegistry.getForId(serverId)!!
            LspInitializationOptions(server)
        }
        composable(
            "${SettingsRoutes.LspServerLogs.route}/{serverId}/{instanceId}",
            arguments =
                listOf(
                    navArgument("serverId", builder = { type = NavType.StringType }),
                    navArgument("instanceId", builder = { type = NavType.StringType }),
                ),
        ) { backStackEntry ->
            val serverId = backStackEntry.arguments?.getString("serverId")!!
            val server = LspRegistry.getForId(serverId)!!
            val instanceId = backStackEntry.arguments?.getString("instanceId")!!
            LspServerLogs(server, instanceId)
        }
        composable(SettingsRoutes.Themes.route) { ThemeScreen(navController) }

        SettingsRegistry.routes.value.forEach { customRoute ->
            composable(customRoute.route, arguments = customRoute.arguments) { backStackEntry ->
                customRoute.content(navController, backStackEntry)
            }
        }
    }
}
