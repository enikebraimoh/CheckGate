package com.escrowafrica.checkgate.ui.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.escrowafrica.checkgate.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escrowafrica.checkgate.ui.DefaultButton
import com.escrowafrica.checkgate.ui.theme.CheckGateTheme
import java.text.DecimalFormat


@Composable
fun DashboardScreen(padding: PaddingValues) {
    Scaffold(
        modifier = Modifier
            .padding(padding)
            .fillMaxSize(),
        backgroundColor = MaterialTheme.colors.primary.copy(alpha = 0.03f),
        topBar = { DashBoardTopBar() }
    ) { padding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
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
                        text = "₦0.00",
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
                        buttonClicked = { },
                        elevation = ButtonDefaults.elevation(
                            defaultElevation = 0.dp,
                            pressedElevation = 0.dp ,
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
            DashboardScreen(padding = PaddingValues(20.dp))
        }
    }
}
