package com.bfoxnet.pocketcode.activities.main.navigation

sealed class MainRoutes(val route: String) {
    object Main : MainRoutes("main")

    object Disclaimer : MainRoutes("t&c")
}
