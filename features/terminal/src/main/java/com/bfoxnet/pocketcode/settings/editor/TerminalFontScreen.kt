package com.bfoxnet.pocketcode.settings.editor

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.bfoxnet.pocketcode.file.child
import com.bfoxnet.pocketcode.file.sandboxDir
import com.bfoxnet.pocketcode.resources.strings
import com.bfoxnet.pocketcode.settings.Settings

@Composable
fun TerminalFontScreen(modifier: Modifier = Modifier) {
    val selectedFontPath = Settings.terminal_font_path
    val etcFontExists = sandboxDir().child("etc/font.ttf").exists()
    val warningMessage = if (etcFontExists) stringResource(strings.terminal_font_warning) else null

    FontScreen(modifier, selectedFontPath, DEFAULT_TERMINAL_FONT_PATH, warningMessage) { font ->
        Settings.terminal_font_path = font.pathOrAsset
        Settings.is_terminal_font_asset = font.isAsset
    }
}
