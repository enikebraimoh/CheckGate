package com.escrowafrica.checkgate.ui.auth.login

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import com.escrowafrica.checkgate.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.escrowafrica.checkgate.StateMachine
import com.escrowafrica.checkgate.ViewModel
import com.escrowafrica.checkgate.ui.*
import com.escrowafrica.checkgate.ui.models.LoginRequest
import com.escrowafrica.checkgate.ui.models.LoginResponse
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navigate: () -> Unit){

    val coroutineScope = rememberCoroutineScope()

     val loginViewModel: ViewModel = hiltViewModel()
     val myState = loginViewModel.loginState.collectAsState(initial = StateMachine.Ideal)

    var ShowError by remember { mutableStateOf(false) }
    var ErrorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false)}

    val (emailText, emailSetText) = remember { mutableStateOf("richardbraimoh@gmail.com") }
    val (passwordText, passwordSetText) = remember { mutableStateOf("000000") }

    suspend fun showErrorMessage() {
        if (!ShowError) {
            ShowError = true
            delay(1000L)
            ShowError = false
        }
        delay(1000)
        ShowError = false
    }


    LaunchedEffect(myState.value) {
        when (myState.value) {
            is StateMachine.Success<LoginResponse> -> {
                isLoading = false
                navigate()
            }
            is StateMachine.Error -> {
                isLoading = false
                ErrorMessage =
                    (myState.value as StateMachine.Error<LoginResponse>).error.message.toString()
                showErrorMessage()
            }
            is StateMachine.GenericError -> {
                isLoading = false
                ErrorMessage =
                    (myState.value as StateMachine.GenericError).error?.message.toString()
                showErrorMessage()
            }

            is StateMachine.Loading -> {
                isLoading = true
            }
            is StateMachine.TimeOut -> {
                isLoading = false
                ErrorMessage =
                    (myState.value as StateMachine.TimeOut<LoginResponse>).error.message.toString()
                showErrorMessage()
            }
            else -> {
                isLoading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(30.dp)
    ) {
        Row(
            modifier = Modifier.align(Alignment.Start),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.ic_back_button),
                contentDescription = "back button"
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Go back",
                style = MaterialTheme.typography.body2
            )
        }

        Spacer(modifier = Modifier.height(60.dp))
        Text(
            text = "Login",
            style = MaterialTheme.typography.h3.copy(fontSize = 26.sp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Return to your account right away",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 14.sp,
                color = Color(
                    0xFF8897A0
                )
            )
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(40.dp))

            DefaultTextField(
                type = TextInputType.EMAIL,
                label = "Email Address",
                placeHolder = "Your Email. e.g hello@gmail.com",
                text = emailText,
                textChanged = { emailSetText(it) })

            Spacer(modifier = Modifier.height(15.dp))

            DefaultTextField(
                label = "Password",
                placeHolder = "Password",
                text = passwordText,
                type = TextInputType.PASSWORD,
                textChanged = { passwordSetText(it) })

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(visible = isLoading) {
                LoadingAnimation(circleSize = 10.dp)
            }
            AnimatedVisibility(visible = !isLoading) {
                DefaultButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    buttonText = "LOG IN",
                    buttonClicked = {
                        coroutineScope.launch {
                            if (Patterns.EMAIL_ADDRESS.matcher(emailText)
                                    .matches() && passwordText.length >= 6
                            ) {
                                loginViewModel.login(
                                    loginDetails =
                                    LoginRequest(
                                        emailText,
                                        passwordText
                                    )
                                )
                            } else {
                                ErrorMessage = "Invalid Email or Password"
                                showErrorMessage()
                            }
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(30.dp))

            Text(
                text = "Forgot Password?",
                style = MaterialTheme.typography.subtitle1.copy(
                    color = MaterialTheme.colors.primary
                )
            )

            Spacer(modifier = Modifier.height(60.dp))

            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    ) {
                        append("Don’t have an account? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colors.primary
                        )
                    ) {
                        append("Create one now")
                    }
                }
            )
        }
    }
    ShowError(shown = ShowError, message = ErrorMessage)

}