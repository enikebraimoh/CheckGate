package com.escrowafrica.checkgate.ui.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escrowafrica.checkgate.ui.DefaultButton
import com.escrowafrica.checkgate.ui.theme.CheckGateTheme
import com.escrowafrica.checkgate.ui.util.ButtonType
import kotlinx.coroutines.delay


@Composable
fun WelcomeScreen(
    getStartedClicked: () -> Unit,
    loginButtonClicked: () -> Unit
) {

    var state by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(0)
        state = !state
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,

        ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(0.45f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                overflow = TextOverflow.Clip,
                textAlign = TextAlign.Center,
                text = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp
                        )
                    ) {
                        append("Check")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 30.sp,
                            color = MaterialTheme.colors.primary
                        )
                    ) {
                        append("Gate")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = "Transforming your business\nis now in your hands!",
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(88.dp))

        AnimatedVisibility(
            visible = !state,
            enter = slideInVertically(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessVeryLow
                )
            ) { fullHeight ->
                fullHeight
            } + fadeIn(
                animationSpec = tween(durationMillis = 1000)
            )
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                DefaultButton(buttonText = "Get Started",
                    buttonClicked = { getStartedClicked() })
                Spacer(modifier = Modifier.height(20.dp))
                DefaultButton(
                    buttonText = "I already have an account",
                    border = 1.dp,
                    type = ButtonType.BORDER,
                    borderColor = MaterialTheme.colors.primary,
                    color = Color.Transparent,
                    buttonClicked = {
                        loginButtonClicked()
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CheckGateTheme {
        Surface {
            WelcomeScreen({}, {})
        }
    }
}