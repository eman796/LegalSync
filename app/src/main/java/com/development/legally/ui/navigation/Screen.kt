package com.development.legally.ui.navigation

sealed class Screen(val route: String) {
    object Welcome : Screen("welcome")
    object Login : Screen("login")
    object Registration : Screen("registration")
    object Cases : Screen("cases")
    object Home : Screen("home")
    object NewCase : Screen("new_case")
    object Agenda : Screen("agenda")
    object Clients : Screen("clients")
}
