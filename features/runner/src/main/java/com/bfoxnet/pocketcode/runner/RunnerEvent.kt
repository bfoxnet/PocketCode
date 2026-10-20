package com.bfoxnet.pocketcode.runner

import com.bfoxnet.pocketcode.events.Event

sealed interface RunnerEvent : Event {

    data class RunnerRun(val runner: Runner) : RunnerEvent
}
