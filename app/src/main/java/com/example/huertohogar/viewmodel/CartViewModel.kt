// CartViewModel.kt
package com.example.huertohogar.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.model.CartItem
import com.example.huertohogar.data.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

class CartViewModel : ViewModel() {

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart

    val cartTotal: StateFlow<Double> = _cart
        .map { list -> list.sumOf { it.subtotal } }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            0.0
        )

    fun addToCart(product: Product) {
        val current = _cart.value.toMutableList()
        val existing = current.find { it.product.id == product.id }
        if (existing != null) {
            val index = current.indexOf(existing)
            current[index] = existing.copy(quantity = existing.quantity + 1)
        } else {
            current.add(CartItem(product, 1))
        }
        _cart.value = current
    }

    fun updateCartItemQuantity(product: Product, quantity: Int) {
        val current = _cart.value.toMutableList()
        val item = current.find { it.product.id == product.id } ?: return
        val index = current.indexOf(item)
        if (quantity > 0) {
            current[index] = item.copy(quantity = quantity)
        } else {
            current.removeAt(index)
        }
        _cart.value = current
    }

    fun removeItemFromCart(product: Product) {
        _cart.value = _cart.value.filter { it.product.id != product.id }
    }

    fun checkout() {
        _cart.value = emptyList()
    }
}
