package com.development.legally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.development.legally.data.repository.AuthRepository
import com.development.legally.ui.agenda.AgendaScreen
import com.development.legally.ui.auth.LoginScreen
import com.development.legally.ui.auth.RegistrationScreen
import com.development.legally.ui.auth.WelcomeScreen
import com.development.legally.ui.cases.CasesScreen
import com.development.legally.ui.cases.NewCaseScreen
import com.development.legally.ui.clients.ClientsScreen
import com.development.legally.ui.clients.EditClientScreen
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

    val authRepository = remember { AuthRepository() }
    val startDestination = if (authRepository.isLoggedIn()) {
        Screen.Home.route
    } else {
        Screen.Welcome.route
    }
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable("edit_client/{clientId}") { backStackEntry ->
            val clientId = backStackEntry.arguments?.getString("clientId")
            EditClientScreen(
                clientId = clientId,
                onNavigateBack = { navController.popBackStack() },
                onNavigateToHome = { navController.navigate(Screen.Home.route) },
                onNavigateToCases = { navController.navigate(Screen.Cases.route) },
                onNavigateToNewCase = { navController.navigate(Screen.NewCase.route) },
                onNavigateToAgenda = { navController.navigate(Screen.Agenda.route) },
                onNavigateToClients = { navController.navigate(Screen.Clients.route) }
            )
        }
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
                },
                onNavigateToNewCase = {
                    navController.navigate(Screen.NewCase.route)
                },
                onNavigateToAgenda = {
                    navController.navigate(Screen.Agenda.route)
                },
                onNavigateToClients = {
                    navController.navigate(Screen.Clients.route)
                },
                onNavigateToEditClient = { clientId ->
                    navController.navigate("edit_client/$clientId")
                }
            )
        }
        composable(Screen.Cases.route) {
            CasesScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route)
                },
                onNavigateToNewCase = {
                    navController.navigate(Screen.NewCase.route)
                },
                onNavigateToAgenda = {
                    navController.navigate(Screen.Agenda.route)
                },
                onNavigateToClients = {
                    navController.navigate(Screen.Clients.route)
                }
            )
        }
        composable(Screen.Agenda.route) {
            AgendaScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route)
                },
                onNavigateToCases = {
                    navController.navigate(Screen.Cases.route)
                },
                onNavigateToNewCase = {
                    navController.navigate(Screen.NewCase.route)
                },
                onNavigateToClients = {
                    navController.navigate(Screen.Clients.route)
                }
            )
        }
        composable(Screen.Clients.route) {
            ClientsScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route)
                },
                onNavigateToCases = {
                    navController.navigate(Screen.Cases.route)
                },
                onNavigateToNewCase = {
                    navController.navigate(Screen.NewCase.route)
                },
                onNavigateToAgenda = {
                    navController.navigate(Screen.Agenda.route)
                },
                onNavigateToEditClient = { clientId ->
                    navController.navigate("edit_client/$clientId")
                }
            )
        }
        composable(Screen.NewCase.route) {
            NewCaseScreen(
                onBackClick = { navController.popBackStack() },
                onCancelClick = { navController.popBackStack() },
                onSaveClick = { navController.popBackStack() }
            )
        }
    }
}