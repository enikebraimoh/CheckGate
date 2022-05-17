package com.escrowafrica.checkgate

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.escrowafrica.checkgate.R
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.escrowafrica.checkgate.ui.cart.CartScreen
import com.escrowafrica.checkgate.ui.dashboard.DashboardScreen

@Composable
fun BottomNavView(
    outerNavHost: NavHostController ) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) { padding ->
        BottomNavGraph(
            padding = padding,
            navController = navController,
            outerNavHost = outerNavHost
        )
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
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarScreen.Home.route
    ) {
        composable(BottomBarScreen.Home.route) {
            DashboardScreen(padding)
        }
        composable(BottomBarScreen.Cart.route) {
            CartScreen(padding)
        }

    }

}