package com.escrowafrica.checkgate.ui.dashboard

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.escrowafrica.checkgate.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.escrowafrica.checkgate.StateMachine
import com.escrowafrica.checkgate.ViewModel
import com.escrowafrica.checkgate.ui.DefaultButton
import com.escrowafrica.checkgate.ui.models.Basket
import com.escrowafrica.checkgate.ui.models.WalletResponseData
import com.escrowafrica.checkgate.ui.theme.CheckGateTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import java.text.DecimalFormat
import kotlinx.coroutines.launch

@Composable
fun DashboardScreen(padding: PaddingValues, depositClicked: () -> Unit) {

    val viewModel: ViewModel = hiltViewModel()

    val (balanceText, balanceSetText) = remember { mutableStateOf("0.00") }

    val context = LocalContext.current

    var isRefreshing = remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.wallet.value) {
        when (viewModel.wallet.value) {
            is StateMachine.Success<WalletResponseData> -> {
                isRefreshing.value = false
                val data =
                    (viewModel.wallet.value as StateMachine.Success<WalletResponseData>).data
                balanceSetText("${data.balance}.00")
            }
            is StateMachine.Error -> {
                isRefreshing.value = false
                val data =
                    (viewModel.wallet.value as StateMachine.Error<WalletResponseData>).error
                Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
            }
            is StateMachine.GenericError -> {
                val data =
                    (viewModel.wallet.value as StateMachine.GenericError).error?.message.toString()
                Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
                isRefreshing.value = false
            }
            is StateMachine.Loading -> {

            }
            is StateMachine.TimeOut -> {
                isRefreshing.value = false
            }
            else -> {

            }
        }
    }

    Scaffold(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.03f),
        topBar = { DashBoardTopBar() }
    ) { padding ->

        SwipeRefresh(
            state = rememberSwipeRefreshState(isRefreshing.value),
            onRefresh = {
                isRefreshing.value = true
                viewModel.getWallet()
            }) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(padding)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    shape = RoundedCornerShape(15.dp),
                    backgroundColor =
                    MaterialTheme.colors.primary
                ) {
                    Column(
                        modifier = Modifier.padding(vertical = 40.dp, horizontal = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Completed Balance",
                            style = MaterialTheme.typography.body1.copy(
                                fontSize = 10.sp,
                                color = Color(
                                    0xFFBECFFB
                                )
                            )
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        Text(
                            text = "$ $balanceText",
                            style = MaterialTheme.typography.h3.copy(
                                fontSize = 28.sp,
                                color = Color.White
                            ),
                            softWrap = false,
                            overflow = TextOverflow.Ellipsis,
                        )

                        Spacer(modifier = Modifier.height(15.dp))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Box(Modifier.weight(1f)) {
                        DefaultButton(
                            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth(),
                            buttonText = "Deposit",
                            buttonClicked = {
                                depositClicked()
                            },
                            elevation = ButtonDefaults.elevation(
                                defaultElevation = 0.dp,
                                pressedElevation = 0.dp,
                                disabledElevation = 0.dp
                            )
                        )
                    }

                    Spacer(modifier = Modifier.width(20.dp))

                    Box(Modifier.weight(1f)) {
                        DefaultButton(
                            color = MaterialTheme.colors.primary.copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth(),
                            buttonText = "Withdraw",
                            buttonClicked = { })
                    }
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(horizontal = 15.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                DefaultButton(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp),
                    innerPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    buttonText = "Start Selling",
                    buttonClicked = {
                        val browserIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://Checkgate.ml/auth/login")
                        )
                        ContextCompat.startActivity(context, browserIntent, null)
                    }
                )
            }
        }
    }
}

@Composable
fun DashBoardTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = "Welcome",
            style = MaterialTheme.typography.body1.copy(
                fontSize = 18.sp,
            )
        )
        Spacer(modifier = Modifier.height(5.dp))
        Image(
            imageVector = ImageVector.vectorResource(id = R.drawable.ic_notification_icon),
            contentDescription = "notifications_icon"
        )

    }

}

@Preview
@Composable
fun DashboardPreview() {
    CheckGateTheme {
        Surface {
            DashboardScreen(padding = PaddingValues(20.dp), {})
        }
    }
}
