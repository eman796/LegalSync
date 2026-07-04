package com.development.legally.ui.auth

import android.graphics.Matrix
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.PathParser
import com.development.legally.R
import com.development.legally.ui.theme.LegallyTheme

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onRequestRegistration: (String, String) -> Unit = { _, _ -> }
) {
    var fullName by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val backgroundColor = Color(red = 0.10980392f, green = 0.14901961f, blue = 0.19607843f, alpha = 1f)
    val goldColor = Color(red = 0.61960787f, green = 0.5529412f, blue = 0.26666668f, alpha = 1f)
    val inputBackgroundColor = Color(0xFF171E27)
    val darkBorderColor = Color(red = 0.09019608f, green = 0.11764706f, blue = 0.15294118f, alpha = 1f)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        // Top handle/bar
        Box(
            modifier = Modifier
                .width(64.dp)
                .height(4.dp)
                .offset(y = 10.dp)
                .align(Alignment.TopCenter)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White)
        )

        // Back Button (Custom Canvas)
        Box(
            modifier = Modifier
                .offset(x = 25.dp, y = 47.dp)
                .size(36.67.dp)
                .clickable { onBackClick() },
            contentAlignment = Alignment.Center
        ) {
            Canvas(
                modifier = Modifier.size(36.67.dp)
            ) {
                val pathData = "M 18.33333396911621 24.88749792416893 C 18.57777843478653 24.88749792416893 18.806945095741085 24.84899842934111 19.020833992958067 24.771998435719798 C 19.23472289017505 24.69622066443628 19.43333365485071 24.56666531518099 19.61666699727375 24.383331991831437 L 24.383334528605154 19.616664956410748 C 24.719445658657296 19.280553861326663 24.88750051339466 18.85277646654182 24.88750051339466 18.333332061767578 C 24.88750051339466 17.813887656993337 24.719445658657296 17.38611026220849 24.383334528605154 17.049999167124405 C 24.04722339855301 16.71388807204032 23.619445959263402 16.54583148638412 23.100001500447615 16.54583148638412 C 22.580557041631828 16.54583148638412 22.15277785393924 16.71388807204032 21.8166667238871 17.049999167124405 L 20.16666736602783 18.69999835332237 L 20.16666736602783 12.833332443237305 C 20.16666736602783 12.313888038463062 19.991279026247415 11.878165300377248 19.640501226615925 11.526165321731606 C 19.28850121134919 11.175387558594053 18.852778427931998 10.999999237060546 18.33333396911621 10.999999237060546 C 17.813889510300424 10.999999237060546 17.37877858596776 11.175387558594053 17.028000786336268 11.526165321731606 C 16.676000771069532 11.878165300377248 16.500000572204588 12.313888038463062 16.500000572204588 12.833332443237305 L 16.500000572204588 18.69999835332237 L 14.850001214345319 17.049999167124405 C 14.513890084293177 16.71388807204032 14.086110896600593 16.54583148638412 13.566666437784805 16.54583148638412 C 13.047221978969018 16.54583148638412 12.619444539679408 16.71388807204032 12.283333409627266 17.049999167124405 C 11.947222279575124 17.38611026220849 11.779167424837759 17.813887656993337 11.779167424837759 18.333332061767578 C 11.779167424837759 18.85277646654182 11.947222279575124 19.280553861326663 12.283333409627266 19.61666699727375 L 17.05000094095867 24.383331991831437 C 17.233334283381712 24.56666531518099 17.43194504805737 24.69622066443628 17.64583394527435 24.771998435719798 C 17.859722842491333 24.84899842934111 18.08888950344589 24.88749792416893 18.33333396911621 24.88749792416893 Z M 18.33333396911621 36.666664123535156 C 15.79722278462516 36.666664123535156 13.413890111711316 36.18510842683052 11.183334420522078 35.22199736124676 C 8.95277872933284 34.26010853848988 7.012500549157471 32.954165736039386 5.3625005356470865 31.304165894190373 C 3.7125005221367022 29.65416605234136 2.4065558354165884 27.713886325624298 1.4446669125874905 25.483330866495816 C 0.4815557468043494 23.252775407367334 0 20.869442982408735 0 18.333332061767578 C 0 15.79722114112642 0.4815557468043494 13.41388871616782 1.4446669125874905 11.183333257039338 C 2.4065558354165884 8.952777797910857 3.7125005221367022 7.012499819596587 5.3625005356470865 5.362499977747575 C 7.012500549157471 3.712500135898563 8.95277872933284 2.4059441904438925 11.183334420522078 1.4428331248601352 C 13.413890111711316 0.480944302103262 15.79722278462516 0 18.33333396911621 0 C 20.869445153607263 0 23.252777826521104 0.480944302103262 25.483333517710342 1.4428331248601352 C 27.71388920889958 2.4059441904438925 29.654169137477922 3.712500135898563 31.304169150988308 5.362499977747575 C 32.954169164498694 7.012499819596587 34.26011210281583 8.952777797910857 35.22200102564493 11.183333257039338 C 36.18511219142807 13.41388871616782 36.66666793823242 15.79722114112642 36.66666793823242 18.333332061767578 C 36.66666793823242 20.869442982408735 36.18511219142807 23.252775407367334 35.22200102564493 25.483330866495816 C 34.26011210281583 27.713886325624298 32.954169164498694 29.65416605234136 31.304169150988308 31.304165894190373 C 29.654169137477922 32.954165736039386 27.71388920889958 34.26010853848988 25.483333517710342 35.22199736124676 C 23.252777826521104 36.18510842683052 20.869445153607263 36.666664123535156 18.33333396911621 36.666664123535156 Z"
                val fillPath = PathParser.createPathFromPathData(pathData)
                val rectF = RectF()
                @Suppress("DEPRECATION")
                fillPath.computeBounds(rectF, true)
                val matrix = Matrix()
                val scale = minOf(size.width / rectF.width(), size.height / rectF.height())
                matrix.setScale(scale, scale)
                fillPath.transform(matrix)
                val composePathFill = fillPath.asComposePath()

                drawPath(
                    path = composePathFill,
                    color = goldColor,
                    style = Fill
                )
                drawPath(
                    path = composePathFill,
                    color = Color.Transparent,
                    style = Stroke(width = 3f, miter = 4f, join = StrokeJoin.Round)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 25.dp)
        ) {
            Spacer(modifier = Modifier.height(288.dp))

            // Full Name Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.full_name_placeholder),
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.width(130.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = goldColor, shape = RoundedCornerShape(64.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = inputBackgroundColor,
                        unfocusedContainerColor = inputBackgroundColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = goldColor
                    ),
                    shape = RoundedCornerShape(64.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Password Input
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = stringResource(id = R.string.password),
                    textAlign = TextAlign.Start,
                    fontSize = 16.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier.width(88.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp)
                        .border(width = 1.dp, color = goldColor, shape = RoundedCornerShape(64.dp)),
                    visualTransformation = PasswordVisualTransformation(),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = inputBackgroundColor,
                        unfocusedContainerColor = inputBackgroundColor,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = goldColor
                    ),
                    shape = RoundedCornerShape(64.dp),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(64.dp))

            // Register Button
            Button(
                onClick = { onRequestRegistration(fullName, password) },
                modifier = Modifier
                    .width(338.dp)
                    .height(60.dp)
                    .border(width = 1.dp, color = darkBorderColor, shape = RoundedCornerShape(64.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = goldColor,
                    contentColor = Color(0xFF171E27)
                ),
                shape = RoundedCornerShape(64.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.request_registration).uppercase(),
                    fontSize = 14.sp,
                    color = Color(0xFF171E27),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 393, heightDp = 852)
@Composable
fun RegistrationScreenPreview() {
    LegallyTheme {
        RegistrationScreen()
    }
}
