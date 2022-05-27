package com.escrowafrica.checkgate.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.escrowafrica.checkgate.R
import com.escrowafrica.checkgate.ui.theme.CheckGateTheme
import com.escrowafrica.checkgate.ui.util.ButtonType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun EmptyComposable() {
    Box(modifier = Modifier.fillMaxSize()) {

    }
}

@Composable
fun EmptyTransactionState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            textAlign = TextAlign.Center,
            text = "You have no Baskets yet",
            style = MaterialTheme.typography.subtitle1.copy(
                fontSize = 20.sp,
                color = Color(0xFF8897A0)
            )
        )
    }
}


@Composable
fun DefaultButton(
    border: Dp = 0.dp,
    type: ButtonType = ButtonType.NORMAL,
    innerPadding: PaddingValues = PaddingValues(15.dp),
    borderColor: Color = MaterialTheme.colors.primary,
    color: Color = MaterialTheme.colors.primary,
    buttonText: String,
    elevation : ButtonElevation = ButtonDefaults.elevation(
        defaultElevation = 0.dp,
        pressedElevation = if (border != 0.dp) 0.dp else 8.dp,
        disabledElevation = 0.dp
    ),
    textSize: TextUnit = MaterialTheme.typography.button.fontSize,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 40.dp),
    buttonClicked: () -> Unit,
    rightIcon: @Composable() (() -> Unit)? = null,
    isEnabled: Boolean = true,
) {
    Button(
        modifier = modifier,
        enabled = isEnabled,
        elevation = elevation,
        border = if (type == ButtonType.BORDER) BorderStroke(border, borderColor) else null,
        colors = ButtonDefaults.buttonColors(
            backgroundColor = color,
            contentColor = if (border != 0.dp) borderColor else Color.White,
            disabledBackgroundColor = Color(0xFFFBE5D9)
        ),
        shape = MaterialTheme.shapes.small.copy(all = CornerSize(25.dp)),
        onClick = { buttonClicked() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .weight(1f), contentAlignment = Alignment.Center
            ) {
                Text(
                    text = buttonText,
                    Modifier.padding(innerPadding),
                    style = MaterialTheme.typography.button.copy(fontSize = textSize),
                    softWrap = false,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            if (rightIcon != null) {
                rightIcon()
            }
        }
    }

}

enum class TextInputType {
    PASSWORD,
    EMAIL,
    TEXT,
    NUMBER,
    PHONE,
    DROPDOWN
}

@Composable
fun DefaultTextField(
    singleLine: Boolean = true,
    type: TextInputType = TextInputType.TEXT,
    text: String,
    isEnabled: Boolean = true,
    click: (() -> Unit)? = null,
    textChanged: (String) -> Unit,
    placeHolder: String = "Default text Field",
    label: String = "Text Field",
    modifier: Modifier = Modifier
) {

    var expanded by remember { mutableStateOf(false) }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }
    val suggestions = listOf("Item1", "Item2", "Item3")

    var transformationType = VisualTransformation.None
    when (type) {
        TextInputType.PASSWORD -> {
            if (passwordVisible) {
                transformationType = VisualTransformation.None
            } else {
                transformationType = PasswordVisualTransformation()
            }
        }
        else -> {}
    }
    Column() {
        OutlinedTextField(
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = when (type) {
                    TextInputType.TEXT -> {
                        KeyboardType.Password
                    }
                    TextInputType.EMAIL -> {
                        KeyboardType.Email
                    }
                    TextInputType.NUMBER -> {
                        KeyboardType.Number
                    }
                    TextInputType.PHONE -> {
                        KeyboardType.Phone
                    }
                    else -> {
                        KeyboardType.Text
                    }
                },
            ),
            value = text,
            enabled = isEnabled,
            modifier = modifier
                .clickable {
                    if (click != null) {
                        click()
                    }
                }
                .fillMaxWidth()
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                },
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(15.dp)),
            visualTransformation = transformationType,
            onValueChange = { textChanged(it) },
            label = {
                Text(
                    modifier = Modifier.padding(top = 3.dp),
                    text = label,
                    style = MaterialTheme.typography.body1.copy(fontSize = 14.sp)
                )
            },
            placeholder = {
                Text(
                    modifier = Modifier.padding(3.dp),
                    text = placeHolder,
                    style = MaterialTheme.typography.body1.copy(fontSize = 14.sp)
                )
            },
            trailingIcon = {
                when (type) {
                    TextInputType.PASSWORD -> {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            if (passwordVisible) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_visible_eye),
                                    contentDescription = ""
                                )
                            } else {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_hidden_eye),
                                    contentDescription = ""
                                )
                            }
                        }
                    }
                    TextInputType.DROPDOWN -> {
                        IconButton(onClick = { expanded = !expanded }) {
                            if (expanded) {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_down),
                                    contentDescription = ""
                                )
                            } else {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_arrow_down),
                                    contentDescription = ""
                                )
                            }
                        }
                    }

                }
            },
        )
        when (type) {
            TextInputType.DROPDOWN -> {
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                ) {
                    suggestions.forEach { label ->
                        DropdownMenuItem(onClick = {
                            textChanged(label)
                        }) {
                            Text(text = label)
                        }
                    }
                }
            }
            else -> null
        }
    }
}

@Composable
fun OtpCell(
    modifier: Modifier = Modifier,
    value: String,
) {
    // UI for OTP Character
    Box(
        modifier = if (value.isNotEmpty()) modifier.border(
            width = 1.2.dp,
            color = MaterialTheme.colors.primary,
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(15.dp))
        )
        else modifier.border(
            width = 1.2.dp,
            color = MaterialTheme.colors.onSecondary,
            shape = MaterialTheme.shapes.small.copy(all = CornerSize(15.dp))
        )

    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(10.dp)
        )
    }
}

@Composable
fun OtpBugView(
    modifier: Modifier = Modifier,
    editTextValue: String,
    otpLength: Int,
    setEditTextValue: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    // hidden TextField for controlling OTP Cells UI
    TextField(
        value = editTextValue,
        onValueChange = { value ->
            if (value.length <= otpLength) {
                setEditTextValue(value)
            }
        },
        modifier = Modifier
            .size(0.dp)
            .focusRequester(focusRequester),
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Number
        )
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        (0 until otpLength).map { index ->
            OtpCell(
                modifier = Modifier
                    .size(80.dp)
                    .padding(10.dp)
                    .clickable {
                        focusRequester.requestFocus()
                        // this is required so if keyboard is dissmissed
                        // then could be open again with focus
                        keyboard?.show()
                    },
                value = editTextValue.getOrNull(index)?.toString() ?: ""
            )
        }
    }

}

@Composable
fun GradientButton(
    gradient: Brush = Brush.verticalGradient(
        colors = listOf(
            Color.Transparent,
            MaterialTheme.colors.surface
        )
    ),
    text: String,
    click: () -> Unit,
) {
    Surface(color = Color.Transparent) {
        Box(
            Modifier
                .background(gradient)
                .padding(bottom = 20.dp, top = 20.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            DefaultButton(
                modifier = Modifier.padding(horizontal = 40.dp),
                rightIcon = {
                    Icon(
                        modifier = Modifier.padding(end = 20.dp),
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_add),
                        contentDescription = "add icon"
                    )
                },
                innerPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                buttonText = text,
                buttonClicked = { click() })
        }
    }


}


@Composable
fun ShowError(message: String, shown: Boolean) {

    AnimatedVisibility(
        visible = shown,
        enter = slideInHorizontally(
            // Enters by sliding down from offset -fullHeight to 0.
            initialOffsetX = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 150, easing = LinearOutSlowInEasing)
        ),
        exit = slideOutHorizontally(
            // Exits by sliding up from offset 0 to -fullHeight.
            targetOffsetX = { fullHeight -> fullHeight },
            animationSpec = tween(durationMillis = 250, easing = FastOutLinearInEasing)
        )
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 30.dp, start = 20.dp),
            color = Color(
                0xFFFCD8D8
            ),
            shape = RoundedCornerShape(
                topStart = 20.dp,
                bottomStart = 20.dp,
                topEnd = 0.dp,
                bottomEnd = 0.dp
            ),
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.ic_warning_sign),
                    contentDescription = "withdraw icon"
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = message,
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 14.sp,
                        color = Color(
                            0xFFF23C3C
                        )
                    )
                )
            }
        }
    }
}


@Preview
@Composable
fun GradientButtonPreview() {
    CheckGateTheme {
        GradientButton(
            text = "Create your first transaction",
            click = {}
        )
    }
}


@Preview
@Composable
fun ErrorPreview() {
    CheckGateTheme {
        Surface {
            ShowError("We couldn’t verify your email", true)
        }
    }
}

@Preview
@Composable
fun DefaultTextFieldPreview() {
    CheckGateTheme {
        Surface {
            DefaultTextField(text = "hello", textChanged = {})
        }
    }
}

@Preview(name = "otp preview", showBackground = true)
@Composable
fun DefaultOtpPreview() {
    CheckGateTheme {
        OtpBugView(otpLength = 4,
            editTextValue = "",
            setEditTextValue = {})
    }
}
