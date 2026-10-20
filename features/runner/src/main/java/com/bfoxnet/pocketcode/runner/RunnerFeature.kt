package com.bfoxnet.pocketcode.runner

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bfoxnet.pocketcode.activities.settings.SettingsRoutes
import com.bfoxnet.pocketcode.commands.CommandProvider
import com.bfoxnet.pocketcode.commands.ToolbarConfiguration
import com.bfoxnet.pocketcode.commands.editor.RunCommand
import com.bfoxnet.pocketcode.components.DialogProvider
import com.bfoxnet.pocketcode.components.DialogRegistry
import com.rk.extension.api.DynamicRoute
import com.bfoxnet.pocketcode.feature.Feature
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.settings.SettingsCategory
import com.bfoxnet.pocketcode.settings.SettingsRegistry
import com.bfoxnet.pocketcode.settings.runners.HtmlRunnerSettings
import com.bfoxnet.pocketcode.settings.runners.RunnerSettings

class RunnerFeature : Feature {
    private var dialogProvider: DialogProvider? = null
    private var settingsCategory: SettingsCategory? = null
    private var runnersRoute: DynamicRoute? = null
    private var htmlRunnersRoute: DynamicRoute? = null

    override fun init(application: Application) {
        // Register RunnerSheet overlay
        dialogProvider =
            DialogProvider {
                val showRunnerDialog by RunnerUI.showRunnerDialog.collectAsStateWithLifecycle()
                if (showRunnerDialog) {
                    RunnerSheet()
                }
            }
                .also { DialogRegistry.register(it) }

        // Register settings category
        settingsCategory =
            SettingsCategory(
                    label = strings.runners.getString(),
                    description = strings.runners_desc.getString(),
                    icon = Icon.ResourceIcon(drawables.run),
                    route = SettingsRoutes.Runners.route,
                )
                .also { SettingsRegistry.registerCategory(it) }

        // Register settings routes
        runnersRoute =
            DynamicRoute(SettingsRoutes.Runners.route) { navController, _ ->
                    RunnerSettings(navController = navController)
                }
                .also { SettingsRegistry.registerRoute(it) }

        htmlRunnersRoute =
            DynamicRoute(SettingsRoutes.HtmlRunner.route) { _, _ -> HtmlRunnerSettings() }
                .also { SettingsRegistry.registerRoute(it) }

        // Register Run command
        CommandProvider.registerCommand(RunCommand)
        ToolbarConfiguration.addGlobalToolbarCommand(RunCommand, 0)
    }

    override fun dispose(application: Application) {
        dialogProvider?.let { DialogRegistry.unregister(it) }
        settingsCategory?.let { SettingsRegistry.unregisterCategory(it) }
        runnersRoute?.let { SettingsRegistry.unregisterRoute(it) }
        htmlRunnersRoute?.let { SettingsRegistry.unregisterRoute(it) }

        CommandProvider.unregisterCommand(RunCommand)
        ToolbarConfiguration.removeGlobalToolbarCommand(RunCommand)
    }
}
