package com.example.huertohogar.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.huertohogar.navigation.Screen
import com.example.huertohogar.viewmodel.CartViewModel
import kotlinx.coroutines.launch
import com.example.huertohogar.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(
    navController: NavController,
    screen: Screen,
    cartViewModel: CartViewModel?,
    authViewModel: AuthViewModel, // <-- agregar
    content: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val cartItems = cartViewModel?.cart?.collectAsState()?.value ?: emptyList()


    val canNavigateBack = navController.previousBackStackEntry != null
    val navItems = listOf(Screen.Products, Screen.Cart, Screen.Profile)
    val isTopLevelDestination = navItems.any { it.route == navController.currentDestination?.route }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(
                navController = navController,
                authViewModel = authViewModel, // <-- pasar aquí
                closeDrawer = { scope.launch { drawerState.close() } }
            )
        },
        gesturesEnabled = isTopLevelDestination
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            screen.title,
                            style = MaterialTheme.typography.titleLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    navigationIcon = {
                        if (canNavigateBack && !isTopLevelDestination) {
                            IconButton(onClick = { navController.navigateUp() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
                            }
                        } else {
                            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                Icon(Icons.Filled.Menu, contentDescription = "Menú")
                            }
                        }
                    },
                    actions = {
                        if (cartViewModel != null) {
                            BadgedBox(
                                badge = {
                                    if (cartItems.isNotEmpty()) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.secondary,
                                            contentColor = MaterialTheme.colorScheme.onSecondary
                                        ) {
                                            Text(cartItems.size.toString())
                                        }
                                    }
                                }
                            ) {
                                IconButton(onClick = { navController.navigate(Screen.Cart.route) }) {
                                    Icon(Icons.Filled.ShoppingCart, contentDescription = "Carrito")
                                }
                            }
                        }
                    }

                )
            },
            bottomBar = {
                if (isTopLevelDestination) {
                    NavigationBar {
                        navItems.forEach { item ->
                            val isSelected = navController.currentDestination?.route == item.route
                            NavigationBarItem(
                                icon = { Icon(item.icon!!, contentDescription = item.title) },
                                label = { Text(item.title) },
                                selected = isSelected,
                                onClick = {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            },
            content = content
        )
    }
}
