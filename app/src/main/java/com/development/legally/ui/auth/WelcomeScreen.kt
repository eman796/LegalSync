package com.development.legally.ui.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.development.legally.R
import com.development.legally.ui.theme.BackgroundDark
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onLoginClick: () -> Unit = {},
    onSignupClick: () -> Unit = {}
) {
    val legallyBlue = Color(0xFF007AFF)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundDark)
            .padding(horizontal = 24.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        LegallyLogo(modifier = Modifier.size(100.dp))

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.welcome_app_name),
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = stringResource(id = R.string.welcome_tagline),
            color = Color.White,
            fontSize = 16.sp,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.weight(1.5f))

        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(width = 2.dp, color = legallyBlue, shape = RoundedCornerShape(28.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = stringResource(id = R.string.login),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSignupClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .border(width = 2.dp, color = Color.White, shape = RoundedCornerShape(28.dp)),
            colors = ButtonDefaults.buttonColors(
                containerColor = legallyBlue,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Text(
                text = stringResource(id = R.string.signup),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun LegallyLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF1E50D6)),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize(0.7f)) {
            val gold = Color(0xFFE5C158)
            val pillarGray = Color(0xFF6E6E6E)

            // Top Bar
            drawRoundRect(
                color = gold,
                topLeft = Offset(0f, 0f),
                size = Size(size.width, size.height * 0.15f),
                cornerRadius = CornerRadius(4.dp.toPx())
            )

            // Capitals (Circles at the ends of the bar)
            drawCircle(
                color = gold,
                radius = size.height * 0.12f,
                center = Offset(size.width * 0.15f, size.height * 0.15f)
            )
            drawCircle(
                color = gold,
                radius = size.height * 0.12f,
                center = Offset(size.width * 0.85f, size.height * 0.15f)
            )

            // Pillars (Vertical lines)
            val pillarCount = 5
            val pillarWidth = size.width * 0.12f
            val spacing = (size.width - (pillarCount * pillarWidth)) / (pillarCount + 1)

            for (i in 0 until pillarCount) {
                val x = spacing + i * (pillarWidth + spacing)
                drawRect(
                    color = pillarGray,
                    topLeft = Offset(x, size.height * 0.25f),
                    size = Size(pillarWidth, size.height * 0.75f)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WelcomeScreenPreview() {
    LegallyTheme {
        WelcomeScreen()
    }
}
