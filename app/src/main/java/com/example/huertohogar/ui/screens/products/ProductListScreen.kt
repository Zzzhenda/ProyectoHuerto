package com.example.huertohogar.ui.screens.products

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext // <-- Importante: Para obtener el contexto
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest // <-- Importante: Para construir la petición con cabeceras
import com.example.huertohogar.R
import com.example.huertohogar.data.model.Product
import com.example.huertohogar.navigation.Screen
import com.example.huertohogar.ui.components.MainScaffold
import com.example.huertohogar.viewmodel.AuthViewModel
import com.example.huertohogar.viewmodel.CartViewModel
import com.example.huertohogar.viewmodel.ProductViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    navController: NavController,
    productViewModel: ProductViewModel,
    cartViewModel: CartViewModel,
    authViewModel: AuthViewModel
) {
    val products by productViewModel.products.collectAsState()
    val selectedCategory by productViewModel.selectedCategory.collectAsState()
    val searchQuery by productViewModel.searchQuery.collectAsState()

    MainScaffold(
        navController = navController,
        screen = Screen.Products,
        cartViewModel = cartViewModel,
        authViewModel = authViewModel
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {

            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { productViewModel.onSearchQueryChange(it) },
                label = { Text("Buscar productos...") },
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp)
            )

            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { product ->
                    ProductItem(product = product) {
                        cartViewModel.addToCart(product)
                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onAddToCart: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            // CORRECCIÓN PARA CARGAR IMÁGENES PROTEGIDAS (Error 403)
            val context = LocalContext.current

            // Construimos una petición personalizada para Coil
            val imageRequest = ImageRequest.Builder(context)
                .data(product.imageUrl)
                // Simulamos ser un navegador Chrome en Windows para evitar el bloqueo simple
                .setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .crossfade(true)
                .build()

            Image(
                painter = rememberAsyncImagePainter(
                    model = imageRequest, // Usamos la request en lugar del string directo
                    error = painterResource(R.drawable.placeholder),
                    placeholder = painterResource(R.drawable.placeholder)
                ),
                contentDescription = product.name,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )


            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(product.name, fontWeight = FontWeight.Bold)
                Text(product.category, style = MaterialTheme.typography.bodySmall)
                Text("CLP $${"%,.0f".format(product.price)}")
            }

            IconButton(onClick = onAddToCart) {
                Icon(Icons.Filled.AddShoppingCart, contentDescription = null)
            }
        }
    }
}