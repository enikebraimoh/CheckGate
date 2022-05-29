package com.escrowafrica.checkgate.ui.cart

import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import androidx.hilt.navigation.compose.hiltViewModel
import com.escrowafrica.checkgate.R
import com.escrowafrica.checkgate.StateMachine
import com.escrowafrica.checkgate.ViewModel
import com.escrowafrica.checkgate.ui.DefaultButton
import com.escrowafrica.checkgate.ui.EmptyTransactionState
import com.escrowafrica.checkgate.ui.dashboard.DashboardScreen
import com.escrowafrica.checkgate.ui.models.Basket
import com.escrowafrica.checkgate.ui.theme.CheckGateTheme
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun CartScreen(padding: PaddingValues) {


    val viewModel: ViewModel = hiltViewModel()

    val context = LocalContext.current

    val listOfTransactions = remember { mutableStateOf(emptyList<Basket>()) }

    val isRefreshing = remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.baskets.value) {
        when (viewModel.baskets.value) {
            is StateMachine.Success<List<Basket>> -> {
                isRefreshing.value = false
                val data =
                    (viewModel.baskets.value as StateMachine.Success<List<Basket>>).data
                listOfTransactions.value = data
            }
            is StateMachine.Error -> {
                isRefreshing.value = false
                val data =
                    (viewModel.baskets.value as StateMachine.Error<List<Basket>>).error
                Toast.makeText(context, data.toString(), Toast.LENGTH_SHORT).show()
            }
            is StateMachine.GenericError -> {
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
        topBar = { BasketScreenTopBar() }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colors.primary.copy(alpha = 0.03f)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (listOfTransactions.value.isEmpty()) {
                    Box(
                        contentAlignment = Alignment.Center,
                    ) {
                        EmptyTransactionState()
                    }
                }
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = isRefreshing.value),
                    onRefresh = {
                        isRefreshing.value = true
                        viewModel.getBaskets()
                    }) {
                    LazyColumn(modifier = Modifier.fillMaxSize()) {
                        items(items = listOfTransactions.value) { basket ->
                            TransactionItem(basket)
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun TransactionItem(item: Basket) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .padding(vertical = 10.dp, horizontal = 20.dp),
        shape = RoundedCornerShape(12.dp)
    ) {

        Column(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 15.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomStart) {
                    Text(
                        text = "Title",
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 16.sp
                        )
                    )
                }

                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomEnd) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.primary
                        )
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp), thickness = 1.dp
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomStart) {
                    Text(
                        text = "Amount",
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 16.sp
                        )
                    )
                }
                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomEnd) {
                    Text(
                        text = "₦ ${item.amount}",
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.primary
                        )
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp), thickness = 1.dp
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomStart) {
                    Text(
                        text = "paid",
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 16.sp
                        )
                    )
                }

                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomEnd) {
                    Text(
                        text = item.paid.toString(),
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.primary
                        )
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp), thickness = 1.dp
            )
            Row(modifier = Modifier.fillMaxWidth()) {
                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomStart) {
                    Text(
                        text = "Description",
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 16.sp
                        )
                    )
                }

                Box(Modifier.weight(1f), contentAlignment = Alignment.BottomEnd) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.body1.copy(
                            fontSize = 16.sp,
                            color = MaterialTheme.colors.primary
                        )
                    )
                }
            }

            DefaultButton(
                innerPadding = PaddingValues(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 20.dp),
                buttonText = "Open Link",
                buttonClicked = {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(item.url))
                    startActivity(context, browserIntent, null)
                }
            )
        }


    }

}

@Composable
fun BasketScreenTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(0.8f),
            text = "Baskets",
            style = MaterialTheme.typography.body1.copy(
                fontSize = 18.sp,
            )
        )
    }

}

@Preview
@Composable
fun BasketItemPreview() {
    CheckGateTheme {
        Surface {
        }
    }
}