package com.development.legally

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.development.legally.data.repository.AuthRepository
import com.development.legally.ui.Editar.EditarCasoScreen
import com.development.legally.ui.Editar.EditarClienteScreen
import com.development.legally.ui.Editar.EditarEventoScreen
import com.development.legally.ui.Nuevo.NuevoCasoScreen
import com.development.legally.ui.Nuevo.NuevoClienteScreen
import com.development.legally.ui.Nuevo.NuevoEventoScreen
import com.development.legally.ui.agenda.AgendaScreen
import com.development.legally.ui.auth.LoginScreen
import com.development.legally.ui.auth.RegistrationScreen
import com.development.legally.ui.auth.WelcomeScreen
import com.development.legally.ui.cases.CasesScreen
import com.development.legally.ui.clients.ClientsScreen
import com.development.legally.ui.home.HomeScreen
import com.development.legally.ui.navigation.Screen
import com.development.legally.ui.theme.LegallyTheme
import com.development.legally.ui.ClasesSupremas.UserSession

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LegallyTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LegallyApp()
                }
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

    val onLogout = {
        authRepository.logout()
        UserSession.clear()
        navController.navigate(Screen.Welcome.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // --- RUTAS DE EDICIÓN ---
        composable("edit_case/{caseId}") { it ->
            val caseId = it.arguments?.getString("caseId")
            EditarCasoScreen(
                caseId = caseId,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() },
                onDelete = { navController.popBackStack() },
                onDuplicate = { }
            )
        }

        composable("edit_event/{eventId}") { it ->
            val eventId = it.arguments?.getString("eventId")
            EditarEventoScreen(
                eventId = eventId,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() }
            )
        }

        composable("edit_client/{clientId}") { it ->
            val clientId = it.arguments?.getString("clientId")
            EditarClienteScreen(
                clientId = clientId,
                onBack = { navController.popBackStack() },
                onSave = { navController.popBackStack() },
                onDelete = { navController.popBackStack() },
                onDuplicate = { }
            )
        }

        // --- RUTAS DE NUEVO ---
        composable(Screen.NewCase.route) { NuevoCasoScreen({navController.popBackStack()},{navController.popBackStack()}) }
        composable(Screen.NewClient.route) { NuevoClienteScreen({navController.popBackStack()},{navController.popBackStack()}) }
        composable(Screen.NewEvent.route) { NuevoEventoScreen({navController.popBackStack()},{navController.popBackStack()}) }

        composable(Screen.Welcome.route) { 
            WelcomeScreen(
                onLoginClick = { navController.navigate(Screen.Login.route) },
                onSignupClick = { navController.navigate(Screen.Registration.route) }
            ) 
        }
        composable(Screen.Login.route) { LoginScreen({navController.navigate(Screen.Home.route){popUpTo(Screen.Login.route){inclusive=true}}},{navController.popBackStack()}) }
        composable(Screen.Registration.route) { RegistrationScreen({navController.popBackStack()}) }

        composable(Screen.Home.route) {
            HomeScreen(
                onLogout = onLogout,
                onNavigateToCases = { navController.navigate(Screen.Cases.route) },
                onNavigateToNewCase = { navController.navigate(Screen.NewCase.route) },
                onNavigateToNewClient = { navController.navigate(Screen.NewClient.route) },
                onNavigateToNewEvent = { navController.navigate(Screen.NewEvent.route) },
                onNavigateToAgenda = { navController.navigate(Screen.Agenda.route) },
                onNavigateToClients = { navController.navigate(Screen.Clients.route) },
                onNavigateToEditClient = { id -> navController.navigate("edit_client/$id") },
                onNavigateToEditCase = { id -> navController.navigate("edit_case/$id") },
                onNavigateToEditEvent = { id -> navController.navigate("edit_event/$id") }
            )
        }
        
        composable(Screen.Cases.route) {
            CasesScreen(
                onLogout = onLogout,
                onNavigateToHome = { navController.navigate(Screen.Home.route) },
                onNavigateToNewCase = { navController.navigate(Screen.NewCase.route) },
                onNavigateToNewClient = { navController.navigate(Screen.NewClient.route) },
                onNavigateToNewEvent = { navController.navigate(Screen.NewEvent.route) },
                onNavigateToAgenda = { navController.navigate(Screen.Agenda.route) },
                onNavigateToClients = { navController.navigate(Screen.Clients.route) },
                onNavigateToEditCase = { id -> navController.navigate("edit_case/$id") }
            )
        }
        
        composable(Screen.Agenda.route) {
            AgendaScreen(
                onLogout = onLogout,
                onNavigateToHome = { navController.navigate(Screen.Home.route) },
                onNavigateToCases = { navController.navigate(Screen.Cases.route) },
                onNavigateToNewCase = { navController.navigate(Screen.NewCase.route) },
                onNavigateToNewClient = { navController.navigate(Screen.NewClient.route) },
                onNavigateToNewEvent = { navController.navigate(Screen.NewEvent.route) },
                onNavigateToClients = { navController.navigate(Screen.Clients.route) },
                onNavigateToEditEvent = { id -> navController.navigate("edit_event/$id") }
            )
        }

        composable(Screen.Clients.route) {
            ClientsScreen(
                onLogout = onLogout,
                onNavigateToHome = { navController.navigate(Screen.Home.route) },
                onNavigateToCases = { navController.navigate(Screen.Cases.route) },
                onNavigateToNewCase = { navController.navigate(Screen.NewCase.route) },
                onNavigateToNewClient = { navController.navigate(Screen.NewClient.route) },
                onNavigateToNewEvent = { navController.navigate(Screen.NewEvent.route) },
                onNavigateToAgenda = { navController.navigate(Screen.Agenda.route) },
                onNavigateToEditClient = { id -> navController.navigate("edit_client/$id") }
            )
        }
    }
}
