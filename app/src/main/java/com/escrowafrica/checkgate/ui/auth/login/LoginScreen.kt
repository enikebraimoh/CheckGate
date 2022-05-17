package com.escrowafrica.checkgate.ui.auth.login

import android.util.Patterns
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
import com.escrowafrica.checkgate.ui.DefaultButton
import com.escrowafrica.checkgate.ui.DefaultTextField
import com.escrowafrica.checkgate.ui.ShowError
import com.escrowafrica.checkgate.ui.TextInputType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(navigate: () -> Unit){

    val coroutineScope = rememberCoroutineScope()
   // val loginViewModel: LoginViewModel = hiltViewModel()
    //val myState = loginViewModel.state.collectAsState(initial = StateMachine.Ideal)

    var ShowError by remember { mutableStateOf(false) }
    var ErrorMessage by remember { mutableStateOf("") }

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

            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth(),
                buttonText = "LOG IN",
                buttonClicked = {
                    coroutineScope.launch {
                        if (Patterns.EMAIL_ADDRESS.matcher(emailText)
                                .matches() && passwordText.length >= 6
                        ) {
                            //navigate()
                            /*loginViewModel.login(
                                loginDetails =
                                LoginDetails(
                                    emailText,
                                    passwordText
                                )
                            )*/
                        } else {
                            ErrorMessage = "Invalid Email or Password"
                            showErrorMessage()
                        }
                    }
                }
            )

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