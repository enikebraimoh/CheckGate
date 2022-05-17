package com.escrowafrica.checkgate


enum class CheckGateScreensScreens {
    WelcomeScreen,
    SignUpScreen,
    LoginScreen,
    DashboardScreen,
}

enum class IconType {
    HOME,
    TRANSACTIONS,
    PRODUCT,
    ACCOUNT
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: IconType
) {
    object Home : BottomBarScreen("Home", "Home", IconType.HOME)
    object Transactions : BottomBarScreen("Transactions", "Transactions", IconType.TRANSACTIONS)
    object Product : BottomBarScreen("Product", "Product", IconType.PRODUCT)
    object Account : BottomBarScreen("Account", "Account", IconType.ACCOUNT)
}