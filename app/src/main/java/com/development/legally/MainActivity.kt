package com.development.legally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.development.legally.ui.auth.LoginScreen
import com.development.legally.ui.auth.RegistrationScreen
import com.development.legally.ui.auth.WelcomeScreen
import com.development.legally.ui.cases.CasesScreen
import com.development.legally.ui.home.HomeScreen
import com.development.legally.ui.navigation.Screen
import com.development.legally.ui.theme.LegallyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LegallyTheme {
                LegallyApp()
            }
        }
    }
}

@Composable
fun LegallyApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Welcome.route
    ) {
        composable(Screen.Welcome.route) {
            WelcomeScreen(
                onLoginClick = {
                    navController.navigate(Screen.Login.route)
                },
                onSignupClick = {
                    navController.navigate(Screen.Registration.route)
                }
            )
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Screen.Registration.route) {
            RegistrationScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onRequestRegistration = { _, _ ->
                    navController.popBackStack()
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCases = {
                    navController.navigate(Screen.Cases.route)
                }
            )
        }
        composable(Screen.Cases.route) {
            CasesScreen()
        }
    }
}
