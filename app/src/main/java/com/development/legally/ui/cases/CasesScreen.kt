package com.development.legally.ui.cases

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.theme.*
import com.development.legally.ui.navigation.LegallyBottomNavigationBar
import com.development.legally.ui.components.*

@Composable
fun CasesScreen(
    onLogout: () -> Unit = {},
    onNavigateToHome: () -> Unit = {},
    onNavigateToNewCase: () -> Unit = {},
    onNavigateToAgenda: () -> Unit = {},
    onNavigateToClients: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 17.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                UserAction(onLogoutConfirm = onLogout)
                SectionHeader(title = "Expedientes", modifier = Modifier.weight(1f))
                NotificationAction()
            }
        },
        bottomBar = { 
            LegallyBottomNavigationBar(
                currentRoute = "cases",
                onInicioClick = onNavigateToHome,
                onExpedientesClick = { /* Already here */ },
                onCrearClick = onNavigateToNewCase,
                onAgendaClick = onNavigateToAgenda,
                onClientesClick = onNavigateToClients
            )
        },
        containerColor = CaseBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 17.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            MainSearchBar(
                title = "Buscar expedientes...",
                onSearch = { /* Implement search */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            val dummyCases = List(10) { "25-000044-033-PE" }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(dummyCases) { caseNumber ->
                    CaseItem(caseNumber)
                }
            }
        }
    }
}

@Composable
fun CaseItem(caseNumber: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceDark),
        border = androidx.compose.foundation.BorderStroke(2.dp, DarkGold),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = painterResource(id = R.drawable.imasghen_del_martillo_lista_expedientes),
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                colorFilter = ColorFilter.tint(DarkGold)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = caseNumber,
                        color = TextWhite,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "ACTIVO",
                        color = DarkGold,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Text(
                    text = "Christian Bullgarelli vs Federico cruz A.K.A Choreco",
                    color = TextWhite,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 8.dp)
                )

                Text(
                    text = "Actualizado: Ayer",
                    color = Color(0xFFBDBDBD),
                    fontSize = 11.sp,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 12.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CasesScreenPreview() {
    LegallyTheme {
        CasesScreen()
    }
}
