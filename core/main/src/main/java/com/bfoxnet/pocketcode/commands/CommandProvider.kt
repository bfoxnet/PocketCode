package com.bfoxnet.pocketcode.commands

import com.bfoxnet.pocketcode.commands.editor.CopyCommand
import com.bfoxnet.pocketcode.commands.editor.CutCommand
import com.bfoxnet.pocketcode.commands.editor.DuplicateLineCommand
import com.bfoxnet.pocketcode.commands.editor.EmulateKeyCommand
import com.bfoxnet.pocketcode.commands.editor.JumpToLineCommand
import com.bfoxnet.pocketcode.commands.editor.LowerCaseCommand
import com.bfoxnet.pocketcode.commands.editor.PasteCommand
import com.bfoxnet.pocketcode.commands.editor.RedoCommand
import com.bfoxnet.pocketcode.commands.editor.RefreshCommand
import com.bfoxnet.pocketcode.commands.editor.ReplaceCommand
import com.bfoxnet.pocketcode.commands.editor.SaveAsCommand
import com.bfoxnet.pocketcode.commands.editor.SaveCommand
import com.bfoxnet.pocketcode.commands.editor.SearchCommand
import com.bfoxnet.pocketcode.commands.editor.SelectAllCommand
import com.bfoxnet.pocketcode.commands.editor.SelectWordCommand
import com.bfoxnet.pocketcode.commands.editor.ShareCommand
import com.bfoxnet.pocketcode.commands.editor.SortLinesAscendingCommand
import com.bfoxnet.pocketcode.commands.editor.SortLinesDescendingCommand
import com.bfoxnet.pocketcode.commands.editor.SyntaxHighlightingCommand
import com.bfoxnet.pocketcode.commands.editor.ToggleReadOnlyCommand
import com.bfoxnet.pocketcode.commands.editor.ToggleWordWrapCommand
import com.bfoxnet.pocketcode.commands.editor.UndoCommand
import com.bfoxnet.pocketcode.commands.editor.UpperCaseCommand
import com.bfoxnet.pocketcode.commands.global.BrowseProjectCommand
import com.bfoxnet.pocketcode.commands.global.CommandPaletteCommand
import com.bfoxnet.pocketcode.commands.global.DocumentationCommand
import com.bfoxnet.pocketcode.commands.global.NewFileCommand
import com.bfoxnet.pocketcode.commands.global.SaveAllCommand
import com.bfoxnet.pocketcode.commands.global.SearchCodeCommand
import com.bfoxnet.pocketcode.commands.global.SearchFileFolderCommand
import com.bfoxnet.pocketcode.commands.global.SettingsCommand
import com.bfoxnet.pocketcode.commands.lsp.FormatDocumentCommand
import com.bfoxnet.pocketcode.commands.lsp.FormatDocumentLspCommand
import com.bfoxnet.pocketcode.commands.lsp.FormatSelectionCommand
import com.bfoxnet.pocketcode.commands.lsp.GoToDefinitionCommand
import com.bfoxnet.pocketcode.commands.lsp.GoToReferencesCommand
import com.bfoxnet.pocketcode.commands.lsp.RenameSymbolCommand
import com.rk.extension.api.DisposableManager
import com.rk.extension.api.Disposer
import com.rk.extension.api.XedExtensionPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

object CommandProvider {
    private val _commandList = MutableStateFlow<List<Command>>(emptyList())
    val commandList: StateFlow<List<Command>> = _commandList.asStateFlow()

    lateinit var DocumentationCommand: DocumentationCommand
    lateinit var BrowseProjectCommand: BrowseProjectCommand
    lateinit var SettingsCommand: SettingsCommand
    lateinit var NewFileCommand: NewFileCommand
    lateinit var CommandPaletteCommand: CommandPaletteCommand
    lateinit var SearchFileFolderCommand: SearchFileFolderCommand
    lateinit var SearchCodeCommand: SearchCodeCommand
    lateinit var CutCommand: CutCommand
    lateinit var CopyCommand: CopyCommand
    lateinit var PasteCommand: PasteCommand
    lateinit var SelectAllCommand: SelectAllCommand
    lateinit var SelectWordCommand: SelectWordCommand
    lateinit var DuplicateLineCommand: DuplicateLineCommand
    lateinit var LowerCaseCommand: LowerCaseCommand
    lateinit var UpperCaseCommand: UpperCaseCommand
    lateinit var SaveCommand: SaveCommand
    lateinit var SaveAsCommand: SaveAsCommand
    lateinit var SaveAllCommand: SaveAllCommand
    lateinit var UndoCommand: UndoCommand
    lateinit var RedoCommand: RedoCommand
    lateinit var ToggleReadOnlyCommand: ToggleReadOnlyCommand
    lateinit var SearchCommand: SearchCommand
    lateinit var ReplaceCommand: ReplaceCommand
    lateinit var RefreshCommand: RefreshCommand
    lateinit var SyntaxHighlightingCommand: SyntaxHighlightingCommand
    lateinit var ToggleWordWrapCommand: ToggleWordWrapCommand
    lateinit var JumpToLineCommand: JumpToLineCommand
    lateinit var SortLinesAscendingCommand: SortLinesAscendingCommand
    lateinit var SortLinesDescendingCommand: SortLinesDescendingCommand
    lateinit var ShareCommand: ShareCommand
    lateinit var EmulateKeyCommand: EmulateKeyCommand
    lateinit var GoToDefinitionCommand: GoToDefinitionCommand
    lateinit var GoToReferencesCommand: GoToReferencesCommand
    lateinit var RenameSymbolCommand: RenameSymbolCommand
    lateinit var FormatDocumentCommand: FormatDocumentCommand
    lateinit var FormatDocumentLspCommand: FormatDocumentLspCommand
    lateinit var FormatSelectionCommand: FormatSelectionCommand

    fun buildCommands() =
        synchronized(this) {
            registerBuiltin(DocumentationCommand()) { DocumentationCommand = it }
            registerBuiltin(BrowseProjectCommand()) { BrowseProjectCommand = it }
            registerBuiltin(SettingsCommand()) { SettingsCommand = it }
            registerBuiltin(NewFileCommand()) { NewFileCommand = it }
            registerBuiltin(CommandPaletteCommand()) { CommandPaletteCommand = it }
            registerBuiltin(SearchFileFolderCommand()) { SearchFileFolderCommand = it }
            registerBuiltin(SearchCodeCommand()) { SearchCodeCommand = it }
            registerBuiltin(CutCommand()) { CutCommand = it }
            registerBuiltin(CopyCommand()) { CopyCommand = it }
            registerBuiltin(PasteCommand()) { PasteCommand = it }
            registerBuiltin(SelectAllCommand()) { SelectAllCommand = it }
            registerBuiltin(SelectWordCommand()) { SelectWordCommand = it }
            registerBuiltin(DuplicateLineCommand()) { DuplicateLineCommand = it }
            registerBuiltin(LowerCaseCommand()) { LowerCaseCommand = it }
            registerBuiltin(UpperCaseCommand()) { UpperCaseCommand = it }
            registerBuiltin(SaveCommand()) { SaveCommand = it }
            registerBuiltin(SaveAsCommand()) { SaveAsCommand = it }
            registerBuiltin(SaveAllCommand()) { SaveAllCommand = it }
            registerBuiltin(UndoCommand()) { UndoCommand = it }
            registerBuiltin(RedoCommand()) { RedoCommand = it }
            registerBuiltin(ToggleReadOnlyCommand()) { ToggleReadOnlyCommand = it }
            registerBuiltin(SearchCommand()) { SearchCommand = it }
            registerBuiltin(ReplaceCommand()) { ReplaceCommand = it }
            registerBuiltin(RefreshCommand()) { RefreshCommand = it }
            registerBuiltin(SyntaxHighlightingCommand()) { SyntaxHighlightingCommand = it }
            registerBuiltin(ToggleWordWrapCommand()) { ToggleWordWrapCommand = it }
            registerBuiltin(JumpToLineCommand()) { JumpToLineCommand = it }
            registerBuiltin(SortLinesAscendingCommand()) { SortLinesAscendingCommand = it }
            registerBuiltin(SortLinesDescendingCommand()) { SortLinesDescendingCommand = it }
            registerBuiltin(ShareCommand()) { ShareCommand = it }
            registerBuiltin(EmulateKeyCommand()) { EmulateKeyCommand = it }
            registerBuiltin(GoToDefinitionCommand()) { GoToDefinitionCommand = it }
            registerBuiltin(GoToReferencesCommand()) { GoToReferencesCommand = it }
            registerBuiltin(RenameSymbolCommand()) { RenameSymbolCommand = it }
            registerBuiltin(FormatDocumentCommand()) { FormatDocumentCommand = it }
            registerBuiltin(FormatDocumentLspCommand()) { FormatDocumentLspCommand = it }
            registerBuiltin(FormatSelectionCommand()) { FormatSelectionCommand = it }
        }

    private fun <T : Command> registerBuiltin(command: T, assign: (T) -> Unit) {
        if (_commandList.value.contains(command)) return
        assign(command)
        _commandList.update { it + command }
        KeybindingsManager.invalidate()
    }

    @XedExtensionPoint
    fun registerCommand(command: Command) {
        val index = _commandList.value.indexOf(command)
        if (index >= 0) {
            _commandList.update { list -> list.toMutableList().also { it[index] = command } }
        } else {
            _commandList.update { it + command }
        }
        KeybindingsManager.invalidate()
    }

    @XedExtensionPoint
    fun unregisterCommand(command: Command) {
        _commandList.update { it - command }
        KeybindingsManager.invalidate()
    }

    private val disposer = Disposer<Command> { unregisterCommand(it) }

    @XedExtensionPoint
    fun registerCommand(command: Command, dm: DisposableManager) {
        registerCommand(command)
        dm.register(command, disposer)
    }

    @XedExtensionPoint
    fun unregisterCommand(command: Command, dm: DisposableManager) {
        unregisterCommand(command)
        dm.unregister(command, disposer)
    }

    fun getForId(id: String): Command? = findRecursive(id, commandList.value)

    fun getParentCommand(command: Command): Command? = findParent(command, commandList.value)

    private fun findParent(target: Command, commands: List<Command>): Command? {
        for (parent in commands) {
            val children = parent.childCommands
            if (children.any { it.id == target.id }) return parent

            val match = findParent(target, children)
            if (match != null) return match
        }
        return null
    }

    private fun findRecursive(id: String, commands: List<Command>): Command? {
        for (command in commands) {
            if (command.id == id) return command
            val children = command.childCommands

            val match = findRecursive(id, children)
            if (match != null) return match
        }
        return null
    }
}
