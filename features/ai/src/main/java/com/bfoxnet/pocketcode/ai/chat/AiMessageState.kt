package com.bfoxnet.pocketcode.ai.chat

import com.bfoxnet.pocketcode.ai.model.AiChatMessage
import com.bfoxnet.pocketcode.ai.model.ToolCallStatus

internal fun AiChatMessage.stopped(): AiChatMessage =
    copy(isStreaming = false, children = children.map { it.stopped() })

internal val ToolCallStatus.isInProgress: Boolean
    get() =
        this == ToolCallStatus.Running ||
            this == ToolCallStatus.AwaitingApproval ||
            this == ToolCallStatus.AwaitingInput
