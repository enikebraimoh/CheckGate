package com.escrowafrica.checkgate

import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import com.escrowafrica.checkgate.R
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.escrowafrica.checkgate.ui.DefaultButton
import com.escrowafrica.checkgate.ui.DefaultTextField
import com.escrowafrica.checkgate.ui.LoadingAnimation
import com.escrowafrica.checkgate.ui.ShowError
import com.escrowafrica.checkgate.ui.TextInputType
import com.escrowafrica.checkgate.ui.cart.CartScreen
import com.escrowafrica.checkgate.ui.dashboard.DashboardScreen
import com.escrowafrica.checkgate.ui.models.DepositData
import com.escrowafrica.checkgate.ui.models.DepositResponse
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BottomNavView(
    outerNavHost: NavHostController
) {
    val navController = rememberNavController()
    val bottomState = rememberModalBottomSheetState(ModalBottomSheetValue.Hidden)
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 15.dp, topEnd = 15.dp),
        sheetState = bottomState,
        sheetContent = {
            DepositBottomSheet(callWebView = { url ->

                val encodedUrl = URLEncoder.encode(url, StandardCharsets.UTF_8.toString())

                coroutineScope.launch { bottomState.animateTo(ModalBottomSheetValue.Hidden) }
                outerNavHost.navigate("${CheckGateScreensScreens.WebViewScreen.name}/$encodedUrl")
            })
        }
    ) {
        Scaffold(
            bottomBar = {
                BottomBar(navController = navController)
            }
        ) { padding ->
            BottomNavGraph(
                padding = padding,
                navController = navController,
                outerNavHost = outerNavHost,
                deposit = {
                    coroutineScope.launch { bottomState.animateTo(ModalBottomSheetValue.Expanded) }
                }
            )
        }
    }

}

@Composable
fun WebView(url: String, goBack: () -> Unit) {
    var backEnabled by remember { mutableStateOf(false) }
    var webView: WebView? = null
    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = { context ->
            WebView(context).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                webViewClient = object : WebViewClient() {
                    override fun onPageStarted(view: WebView, url: String?, favicon: Bitmap?) {
                        backEnabled = view.canGoBack()
                    }

                    override fun onPageFinished(view: WebView?, url: String?) {
                        if (url.toString().contains("https://www.rapyd.net/") || url.toString()
                                .contains("https://deposit.checkgate.ml/close")
                        ) {
                            goBack()
                        }
                    }
                }

                settings.javaScriptEnabled = true

                loadUrl(url)
                webView = this
            }
        }, update = {
            webView = it
        })

    BackHandler(enabled = backEnabled) {
        webView?.goBack()
    }
}


@Composable
fun DepositBottomSheet(callWebView: (amount: String) -> Unit) {

    val viewModel: ViewModel = hiltViewModel()
    val myState = viewModel.depositState.collectAsState(initial = StateMachine.Ideal)
    var isLoading by remember { mutableStateOf(false) }
    var ShowError by remember { mutableStateOf(false) }
    var ErrorMessage by remember { mutableStateOf("") }


    val coroutineScope = rememberCoroutineScope()
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
            is StateMachine.Success<DepositResponse> -> {
                isLoading = false
                val url = (myState.value as StateMachine.Success<DepositResponse>).data.data.page
                callWebView(url)
            }
            is StateMachine.Error -> {
                isLoading = false
                ErrorMessage =
                    (myState.value as StateMachine.Error<DepositResponse>).error.message.toString()
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
                    (myState.value as StateMachine.TimeOut<DepositResponse>).error.message.toString()
                showErrorMessage()
            }
            else -> {
                isLoading = false
            }
        }
    }


    var (depositAmount, setDepositAmount) = remember { mutableStateOf("") }

    ShowError(shown = ShowError, message = ErrorMessage)

    Surface(
        modifier = Modifier.padding(bottom = 30.dp),
        shape = RoundedCornerShape(topEnd = 10.dp, topStart = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(top = 30.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 10.dp),
                text = "Enter a Deposit Amount",
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(40.dp))
            DefaultTextField(
                isEnabled = !isLoading,
                label = "deposit Amount",
                type = TextInputType.NUMBER,
                placeHolder = "Enter Deposit Amount",
                text = depositAmount,
                textChanged = setDepositAmount
            )
            Spacer(modifier = Modifier.height(20.dp))

            Spacer(modifier = Modifier.height(30.dp))
            Column(
                modifier = Modifier.padding(top = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(visible = isLoading) {
                    LoadingAnimation(circleSize = 10.dp)
                }
                AnimatedVisibility(visible = !isLoading) {
                    DefaultButton(
                        modifier = Modifier.fillMaxWidth(),
                        innerPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        buttonText = "Deposit",
                        buttonClicked = {
                            coroutineScope.launch {
                                if (depositAmount.isNotBlank()) {
                                    val amount = DepositData(amount = depositAmount.toInt())
                                    viewModel.deposit(amount = amount)
                                } else {
                                    ErrorMessage = "Invalid Amount"
                                    showErrorMessage()
                                }
                            }
                        }
                    )
                }
            }

        }


    }

}


@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Cart
    )

    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = backStackEntry?.destination

    BottomNavigation(
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        modifier = Modifier.fillMaxSize(),
        selectedContentColor = MaterialTheme.colors.primary,
        unselectedContentColor = MaterialTheme.colors.primary.copy(alpha = 0.3f),
        label = {
            Text(
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxSize(),
                text = screen.title,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.body1.copy(fontSize = 12.sp)
            )
        },
        icon = {
            when (screen.icon) {
                IconType.HOME -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_home_icon),
                        contentDescription = screen.title
                    )
                }

                IconType.Cart -> {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.ic_cart),
                        contentDescription = screen.title
                    )
                }

            }

        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
    )
}

@Composable
fun BottomNavGraph(
    outerNavHost: NavHostController,
    padding: PaddingValues,
    deposit: () -> Unit,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(BottomBarScreen.Home.route) {
            DashboardScreen(padding,
                depositClicked = {
                    deposit()
                })
        }
        composable(BottomBarScreen.Cart.route) {
            CartScreen(padding)
        }

    }

}