package com.example.huertohogar.navigation

import android.app.Application
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.huertohogar.ui.screens.about.AboutScreen
import com.example.huertohogar.ui.screens.addresses.AddressesScreen
import com.example.huertohogar.ui.screens.cart.CartScreen
import com.example.huertohogar.ui.screens.login.LoginScreen
import com.example.huertohogar.ui.screens.orders.OrdersScreen
import com.example.huertohogar.ui.screens.products.ProductListScreen
import com.example.huertohogar.ui.screens.profile.ProfileScreen
import com.example.huertohogar.ui.screens.register.RegisterScreen
import com.example.huertohogar.ui.screens.settings.SettingsScreen
import com.example.huertohogar.viewmodel.AuthViewModel
import com.example.huertohogar.viewmodel.CartViewModel
import com.example.huertohogar.viewmodel.CategoryViewModel
import com.example.huertohogar.viewmodel.OrderViewModel
import com.example.huertohogar.viewmodel.ProductViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val application = context.applicationContext as Application

    // ViewModels
    val authViewModel: AuthViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application))
    val productViewModel: ProductViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application))
    val categoryViewModel: CategoryViewModel = viewModel(factory = ViewModelProvider.AndroidViewModelFactory.getInstance(application)) // <-- NUEVO
    val cartViewModel: CartViewModel = viewModel()
    val orderViewModel: OrderViewModel = viewModel()

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()
    val startDestination = Screen.Products.route

    NavHost(navController = navController, startDestination = startDestination) {

        composable(Screen.Login.route) {
            LoginScreen(navController, authViewModel)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController, authViewModel)
        }

        composable(Screen.Products.route) {
            ProductListScreen(
                navController = navController,
                productViewModel = productViewModel,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel,
                categoryViewModel = categoryViewModel
            )
        }

        composable(Screen.Cart.route) {
            CartScreen(
                navController = navController,
                cartViewModel = cartViewModel,
                authViewModel = authViewModel
            )
        }

        // ... resto de rutas (Profile, Orders, Addresses, Settings, About) se mantienen igual ...

        composable(Screen.Profile.route) {
            if (!isLoggedIn) navController.navigate(Screen.Login.route) { popUpTo(0) }
            else ProfileScreen(navController, authViewModel)
        }
        composable(Screen.Orders.route) {
            if (!isLoggedIn) navController.navigate(Screen.Login.route) { popUpTo(0) }
            else OrdersScreen(navController, orderViewModel)
        }
        composable(Screen.Addresses.route) {
            if (!isLoggedIn) navController.navigate(Screen.Login.route) { popUpTo(0) }
            else AddressesScreen(navController)
        }
        composable(Screen.Settings.route) {
            if (!isLoggedIn) navController.navigate(Screen.Login.route) { popUpTo(0) }
            else SettingsScreen(navController)
        }
        composable(Screen.About.route) {
            AboutScreen(navController, authViewModel)
        }
    }
}