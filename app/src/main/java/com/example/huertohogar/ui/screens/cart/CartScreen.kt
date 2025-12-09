package com.example.huertohogar.ui.screens.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.huertohogar.data.model.CartItem
import com.example.huertohogar.navigation.Screen
import com.example.huertohogar.ui.components.MainScaffold
import com.example.huertohogar.viewmodel.AuthViewModel
import com.example.huertohogar.viewmodel.CartViewModel

@Composable
fun CartScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel(),
    authViewModel: AuthViewModel
) {
    val cartItems by cartViewModel.cart.collectAsState()
    val cartTotal by cartViewModel.cartTotal.collectAsState()

    MainScaffold(
        navController = navController,
        screen = Screen.Cart,
        cartViewModel = cartViewModel,
        authViewModel = authViewModel
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            Icons.Filled.ShoppingCart,
                            contentDescription = "Carrito Vacío",
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Tu carrito está vacío.", style = MaterialTheme.typography.titleLarge)
                        Text("¡Añade productos frescos del campo!", style = MaterialTheme.typography.bodyLarge)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(cartItems, key = { it.product.id }) { item ->
                        CartItemView(
                            item = item,
                            onQuantityChange = { newQty -> cartViewModel.updateCartItemQuantity(item.product, newQty) },
                            onRemoveItem = { cartViewModel.removeItemFromCart(item.product) }
                        )
                    }
                }

                CartSummary(cartTotal = cartTotal, onCheckout = cartViewModel::checkout)
            }
        }
    }
}

/** Vista de un solo artículo en el carrito */
@Composable
fun CartItemView(item: CartItem, onQuantityChange: (Int) -> Unit, onRemoveItem: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(item.product.name, style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp))
                Text(
                    "CLP $${"%,.0f".format(item.product.price)}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                )
                Text(
                    "Subtotal: CLP $${"%,.0f".format(item.subtotal)}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { onQuantityChange(item.quantity - 1) },
                    enabled = item.quantity > 1
                ) {
                    Icon(Icons.Filled.RemoveCircle, contentDescription = "Restar", tint = MaterialTheme.colorScheme.primary)
                }
                Text(
                    item.quantity.toString(),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.width(30.dp),
                    textAlign = TextAlign.Center
                )
                IconButton(
                    onClick = { onQuantityChange(item.quantity + 1) },
                    enabled = item.quantity < item.product.stock
                ) {
                    Icon(Icons.Filled.AddCircle, contentDescription = "Sumar", tint = MaterialTheme.colorScheme.primary)
                }
                IconButton(onClick = onRemoveItem) {
                    Icon(Icons.Filled.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.7f))
                }
            }
        }
    }
}

/** Componente de resumen del carrito y botón de pago */
@Composable
fun CartSummary(cartTotal: Double, onCheckout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.End
    ) {
        Text(
            "Total Carrito:",
            style = MaterialTheme.typography.titleLarge.copy(color = MaterialTheme.colorScheme.onSurfaceVariant)
        )
        Text(
            "CLP $${"%,.0f".format(cartTotal)}",
            style = MaterialTheme.typography.headlineSmall.copy(color = MaterialTheme.colorScheme.primary)
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onCheckout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                "Confirmar Pedido y Pagar",
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.labelLarge.copy(fontSize = 18.sp)
            )
            Spacer(Modifier.width(8.dp))
            Icon(Icons.Filled.Done, contentDescription = "Pagar", tint = MaterialTheme.colorScheme.onSecondary)
        }
    }
}
