package com.escrowafrica.checkgate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.core.tween
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.escrowafrica.checkgate.ui.auth.login.LoginScreen
import com.escrowafrica.checkgate.ui.welcome.WelcomeScreen
import com.escrowafrica.checkgate.ui.auth.signup.SignupScreen
import com.escrowafrica.checkgate.ui.theme.CheckGateTheme
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        setContent {
            CheckGateTheme {
                // A surface container using the 'background' color from the theme
                Surface {
                    val navController = rememberAnimatedNavController()
                    NavGraph(
                        navController = navController,
                        startDestination = CheckGateScreensScreens.WelcomeScreen.name
                    )
                }
            }
        }


    }
}


@Composable
fun NavGraph(
    startDestination: String,
    navController: NavHostController
) {
    AnimatedNavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(
            CheckGateScreensScreens.WelcomeScreen.name,
            enterTransition = {
                when (initialState.destination.route) {
                    CheckGateScreensScreens.SignUpScreen.name ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    CheckGateScreensScreens.LoginScreen.name ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    CheckGateScreensScreens.SignUpScreen.name ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    CheckGateScreensScreens.LoginScreen.name ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
        ) {
            WelcomeScreen(
                getStartedClicked = {
                    navController.navigate(CheckGateScreensScreens.SignUpScreen.name)
                },
                loginButtonClicked = {
                    navController.navigate(CheckGateScreensScreens.LoginScreen.name)
                }
            )
        }
        composable(
            CheckGateScreensScreens.SignUpScreen.name,
            enterTransition = {
                when (initialState.destination.route) {
                    CheckGateScreensScreens.WelcomeScreen.name ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    CheckGateScreensScreens.WelcomeScreen.name ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            }
        ) {
            SignupScreen(
                continueButtonClicked = {
                    navController.navigate(CheckGateScreensScreens.DashboardScreen.name)
                }
            )
        }
        composable(CheckGateScreensScreens.LoginScreen.name,
            enterTransition = {
                when (initialState.destination.route) {
                    CheckGateScreensScreens.WelcomeScreen.name ->
                        slideIntoContainer(
                            AnimatedContentScope.SlideDirection.Left,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when (targetState.destination.route) {
                    CheckGateScreensScreens.WelcomeScreen.name ->
                        slideOutOfContainer(
                            AnimatedContentScope.SlideDirection.Right,
                            animationSpec = tween(300)
                        )
                    else -> null
                }
            }
        ) {
            LoginScreen(
                navigate = {
                    navController.navigate(CheckGateScreensScreens.DashboardScreen.name)
                }
            )
        }

        composable(
            CheckGateScreensScreens.DashboardScreen.name
        ) {
            BottomNavView(navController)
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CheckGateTheme {

    }
}