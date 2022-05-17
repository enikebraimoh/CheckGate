package com.escrowafrica.checkgate.ui.auth.signup

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import com.escrowafrica.checkgate.ui.DefaultButton
import com.escrowafrica.checkgate.ui.DefaultTextField
import com.escrowafrica.checkgate.ui.TextInputType

@Composable
fun SignupScreen(
    continueButtonClicked: () -> Unit){

    val (nameText, nameSetText) = remember { mutableStateOf("") }
    val (emailText, emailSetText) = remember { mutableStateOf("") }
    val (phoneText, phoneSetText) = remember { mutableStateOf("") }
    val (passwordText, passwordSetText) = remember { mutableStateOf("") }
    val (confirmPassswordText, confirmPasswordSetText) = remember { mutableStateOf("") }


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
                label = "Your Name",
                placeHolder = "Your Name. e.g FirstName",
                text = nameText,
                textChanged = { nameSetText(it) })
            Spacer(modifier = Modifier.height(15.dp))
            DefaultTextField(
                label = "Your Email",
                placeHolder = "Your Email. e.g holder@gmail.com",
                text = emailText,
                textChanged = { emailSetText(it) })
            Spacer(modifier = Modifier.height(15.dp))
            DefaultTextField(
                label = "Phone number",
                placeHolder = "Phone number. e.g 08140252210",
                text = phoneText,
                textChanged = { phoneSetText(it) })

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
                text = confirmPassswordText,
                textChanged = { confirmPasswordSetText(it) })
            Spacer(modifier = Modifier.height(40.dp))
            DefaultButton(
                modifier = Modifier
                    .fillMaxWidth(),
                buttonText = "Continue",
                buttonClicked = {
                    continueButtonClicked()
                })

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


}