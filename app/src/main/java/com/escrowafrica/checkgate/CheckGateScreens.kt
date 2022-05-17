package com.escrowafrica.checkgate


enum class CheckGateScreensScreens {
    WelcomeScreen,
    SignUpScreen,
    LoginScreen,
    DashboardScreen,
}

enum class IconType {
    HOME,
    Cart
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: IconType
) {
    object Home : BottomBarScreen("Home", "Home", IconType.HOME)
    object Cart : BottomBarScreen("Cart", "Cart", IconType.Cart)
}