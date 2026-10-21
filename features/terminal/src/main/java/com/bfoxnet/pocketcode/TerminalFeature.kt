package com.bfoxnet.pocketcode

import android.app.Application
import android.content.Intent
import com.bfoxnet.pocketcode.activities.main.MainActivity
import com.bfoxnet.pocketcode.activities.settings.SettingsRoutes
import com.bfoxnet.pocketcode.activities.terminal.Terminal
import com.bfoxnet.pocketcode.commands.CommandProvider
import com.bfoxnet.pocketcode.commands.ToolbarConfiguration
import com.bfoxnet.pocketcode.commands.global.TerminalCommand
import com.bfoxnet.pocketcode.drawer.AddProjectCategory
import com.bfoxnet.pocketcode.drawer.AddProjectOption
import com.bfoxnet.pocketcode.drawer.AddProjectRegistry
import com.bfoxnet.pocketcode.exec.pendingCommand
import com.bfoxnet.pocketcode.exec.ubuntuProcess
import com.rk.extension.api.DynamicRoute
import com.bfoxnet.pocketcode.feature.Feature
import com.bfoxnet.pocketcode.feature.FeatureRegistry
import com.bfoxnet.pocketcode.feature.FeatureToggle
import com.bfoxnet.pocketcode.file.FileObject
import com.bfoxnet.pocketcode.file.FileWrapper
import com.bfoxnet.pocketcode.file.sandboxHomeDir
import com.bfoxnet.pocketcode.filetree.FileAction
import com.bfoxnet.pocketcode.filetree.FileActionContext
import com.bfoxnet.pocketcode.filetree.FileActionProvider
import com.bfoxnet.pocketcode.filetree.FileActionType
import com.bfoxnet.pocketcode.icons.Icon
import com.bfoxnet.pocketcode.lsp.LspRegistry
import com.bfoxnet.pocketcode.lsp.servers.Bash
import com.bfoxnet.pocketcode.lsp.servers.CSS
import com.bfoxnet.pocketcode.lsp.servers.Emmet
import com.bfoxnet.pocketcode.lsp.servers.HTML
import com.bfoxnet.pocketcode.lsp.servers.TypeScript
import com.bfoxnet.pocketcode.lsp.servers.XML
import com.bfoxnet.pocketcode.resources.drawables
import com.bfoxnet.pocketcode.resources.getString
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.runner.RunnerManager
import com.bfoxnet.pocketcode.runner.runners.UniversalRunner
import com.bfoxnet.pocketcode.settings.Settings
import com.bfoxnet.pocketcode.settings.SettingsCategory
import com.bfoxnet.pocketcode.settings.SettingsRegistry
import com.bfoxnet.pocketcode.settings.editor.TerminalFontScreen
import com.bfoxnet.pocketcode.settings.terminal.SettingsTerminalScreen
import com.bfoxnet.pocketcode.settings.terminal.TerminalCheckScreen
import com.bfoxnet.pocketcode.settings.terminal.TerminalExtraKeys
import com.bfoxnet.pocketcode.utils.dialogRes
import com.bfoxnet.pocketcode.utils.toast

class TerminalFeature : Feature {
    override val toggle =
        FeatureToggle(
            name = strings.terminal_feature.getString(),
            key = "feature_terminal",
            default = true,
            icon = Icon.ResourceIcon(drawables.terminal),
        )

    private var settingsCategory: SettingsCategory? = null
    private var addProjectOption: AddProjectOption? = null
    private val routes = mutableListOf<DynamicRoute>()

    override fun init(application: Application) {

        // Register the file action
        FileActionProvider.registerAction(TerminalAction)

        // Register settings categories
        settingsCategory =
            SettingsCategory(
                    label = strings.terminal.getString(),
                    description = strings.terminal_desc.getString(),
                    icon = Icon.ResourceIcon(drawables.terminal),
                    route = SettingsRoutes.TerminalSettings.route,
                )
                .also { SettingsRegistry.registerCategory(it) }

        if (FeatureRegistry.isEnabled("feature_terminal")) {
            addProjectOption =
                AddProjectOption(
                        icon = Icon.ResourceIcon(drawables.terminal),
                        title = strings.terminal_home.getString(),
                        description = strings.terminal_home_desc.getString(),
                        category = AddProjectCategory.STORAGE,
                        onClick = { onDismiss ->
                            if (!Settings.has_shown_terminal_dir_warning) {
                                dialogRes(
                                    title = strings.attention.getString(),
                                    msg = strings.warning_private_dir.getString(),
                                    onOk = {
                                        Settings.has_shown_terminal_dir_warning = true
                                        MainActivity.instance
                                            ?.drawerViewModel
                                            ?.addFileTreeTab(FileWrapper(sandboxHomeDir()), true)
                                    },
                                )
                            } else {
                                MainActivity.instance
                                    ?.drawerViewModel
                                    ?.addFileTreeTab(FileWrapper(sandboxHomeDir()), true)
                            }
                            onDismiss()
                        },
                    )
                    .also { AddProjectRegistry.register(it) }
        }

        // Register settings routes
        routes.add(DynamicRoute(SettingsRoutes.TerminalSettings.route) { _, _ -> SettingsTerminalScreen() })
        routes.add(DynamicRoute(SettingsRoutes.TerminalExtraKeys.route) { _, _ -> TerminalExtraKeys() })
        routes.add(DynamicRoute(SettingsRoutes.TerminalCheck.route) { _, _ -> TerminalCheckScreen() })
        routes.add(DynamicRoute(SettingsRoutes.TerminalFontScreen.route) { _, _ -> TerminalFontScreen() })

        routes.forEach { SettingsRegistry.registerRoute(it) }

        // Register UniversalRunner dynamically
        RunnerManager.addBuiltInRunner(UniversalRunner)

        // Register TerminalLauncher handler
        TerminalLauncher.handler = { activity, sandbox, exe, args, id, terminatePreviousSession, workingDir, env ->
            pendingCommand =
                com.bfoxnet.pocketcode.exec.TerminalCommand(
                    sandbox = sandbox,
                    exe = exe,
                    args = args,
                    id = id,
                    terminatePreviousSession = terminatePreviousSession,
                    workingDir = workingDir,
                    env = env,
                )
            try {
                val intent = Intent(activity, Terminal::class.java)
                activity.startActivity(intent)
            } catch (_: Exception) {
                toast("Terminal feature is not available in this build")
            }
        }

        // Register SandboxedProcessRegistry provider
        SandboxedProcessRegistry.provider = { command, workingDir, excludeMounts ->
            ubuntuProcess(excludeMounts, workingDir = workingDir, command = command)
        }

        // Register global command
        CommandProvider.registerCommand(TerminalCommand)

        // Assuming there's at least one item already there
        ToolbarConfiguration.addGlobalToolbarCommand(TerminalCommand, index = 1)

        // Register built-in LSP servers
        LspRegistry.addBuiltInServers(HTML, Emmet, CSS, TypeScript, Bash, XML)
    }

    override fun dispose(application: Application) {
        FileActionProvider.unregisterAction(TerminalAction)
        settingsCategory?.let { SettingsRegistry.unregisterCategory(it) }
        addProjectOption?.let { AddProjectRegistry.unregister(it) }
        routes.forEach { SettingsRegistry.unregisterRoute(it) }
        routes.clear()

        RunnerManager.removeBuiltInRunner(UniversalRunner)
        TerminalLauncher.handler = null
        SandboxedProcessRegistry.provider = null
        CommandProvider.unregisterCommand(TerminalCommand)
        ToolbarConfiguration.removeGlobalToolbarCommand(TerminalCommand)
        LspRegistry.removeBuiltInServers(HTML, Emmet, CSS, TypeScript, Bash, XML)
    }
}

object TerminalAction : FileAction() {
    override val icon = Icon.ResourceIcon(drawables.terminal)
    override val title = strings.open_in_terminal.getString()

    override suspend fun execute(context: FileActionContext) {
        val file = context.file
        val ctx = context.context

        val intent = Intent(ctx, Terminal::class.java)
        intent.putExtra("cwd", file.getAbsolutePath())
        ctx.startActivity(intent)
    }

    override suspend fun isSupported(file: FileObject, root: FileObject?): Boolean {
        return file is FileWrapper && FeatureRegistry.isEnabled("feature_terminal")
    }

    override val type = FileActionType(file = false, folder = true, rootFolder = true)
}
