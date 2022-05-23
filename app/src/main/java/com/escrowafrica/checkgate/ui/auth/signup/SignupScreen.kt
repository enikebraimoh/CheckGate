package com.escrowafrica.checkgate.ui.auth.signup

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
import com.escrowafrica.checkgate.R
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.escrowafrica.checkgate.StateMachine
import com.escrowafrica.checkgate.ViewModel
import com.escrowafrica.checkgate.ui.*
import com.escrowafrica.checkgate.ui.models.LoginResponse
import com.escrowafrica.checkgate.ui.models.SignUpRequest
import com.escrowafrica.checkgate.ui.models.SignUpResponse
import kotlinx.coroutines.delay

@Composable
fun SignupScreen(navigate: () -> Unit){

    val coroutineScope = rememberCoroutineScope()

    val viewModel: ViewModel = hiltViewModel()
    val myState = viewModel.signUpState.collectAsState(initial = StateMachine.Ideal)

    var ShowError by remember { mutableStateOf(false) }
    var ErrorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    val (lastNameText, lastNameSetText) = remember { mutableStateOf("") }
    val (firstNameText, firstNameSetText) = remember { mutableStateOf("") }
    val (emailText, emailSetText) = remember { mutableStateOf("") }
    val (passwordText, passwordSetText) = remember { mutableStateOf("") }
    val (confirmPasswordText, confirmPasswordSetText) = remember { mutableStateOf("") }

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
            is StateMachine.Success<SignUpResponse> -> {
                isLoading = false
                navigate()
            }
            is StateMachine.Error -> {
                isLoading = false
                ErrorMessage =
                    (myState.value as StateMachine.Error<SignUpResponse>).error.message.toString()
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
                    (myState.value as StateMachine.TimeOut<SignUpResponse>).error.message.toString()
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
            text = "Create new account",
            style = MaterialTheme.typography.h3.copy(fontSize = 26.sp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Make sales, receive payments and\ndo more on the escrow app",
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
                label = "Your First Name",
                placeHolder = "Your Name. e.g FirstName",
                text = firstNameText,
                textChanged = firstNameSetText)
            Spacer(modifier = Modifier.height(15.dp))
            DefaultTextField(
                label = "Your Last Name",
                placeHolder = "Your Name. e.g LastName",
                text = lastNameText,
                textChanged = lastNameSetText)
            Spacer(modifier = Modifier.height(15.dp))
            DefaultTextField(
                label = "Your Email",
                placeHolder = "Your Email. e.g holder@gmail.com",
                text = emailText,
                textChanged = { emailSetText(it) })
            Spacer(modifier = Modifier.height(15.dp))
            DefaultTextField(
                type = TextInputType.PASSWORD,
                label = "Password",
                placeHolder = "Password",
                text = passwordText,
                textChanged = { passwordSetText(it) })

            Spacer(modifier = Modifier.height(15.dp))
            DefaultTextField(
                type = TextInputType.PASSWORD,
                label = "Confirm Password",
                placeHolder = "Confirm Password",
                text = confirmPasswordText,
                textChanged = { confirmPasswordSetText(it) })
            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(visible = isLoading) {
                LoadingAnimation(circleSize = 10.dp)
            }
            AnimatedVisibility(visible = !isLoading) {
                DefaultButton(
                    modifier = Modifier
                        .fillMaxWidth(),
                    buttonText = "Continue",
                    buttonClicked = {
                        viewModel.signUp(
                            signUpDetails = SignUpRequest(
                                confirmPassword = confirmPasswordText,
                                email = emailText,
                                first_name = firstNameText,
                                last_name = lastNameText,
                                password = passwordText
                            )
                        )
                    })

            }

            Spacer(modifier = Modifier.height(30.dp))
            Text(
                buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    ) {
                        append("Already have an account? ")
                    }
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = MaterialTheme.colors.primary
                        )
                    ) {
                        append("Log in")
                    }
                }
            )
        }
    }

    ShowError(shown = ShowError, message = ErrorMessage)

}