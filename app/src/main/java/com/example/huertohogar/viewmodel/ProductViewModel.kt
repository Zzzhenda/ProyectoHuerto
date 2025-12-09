// ProductViewModel.kt
package com.example.huertohogar.viewmodel


import androidx.lifecycle.viewModelScope

import com.example.huertohogar.repository.ProductRepository
import com.example.huertohogar.data.db.toProductModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.db.AppDatabase
import com.example.huertohogar.data.model.Product
import com.example.huertohogar.data.network.RetrofitInstance
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProductViewModel(application: Application) : AndroidViewModel(application) {

    private val productDao = AppDatabase.getInstance(application).productDao()
    private val productRepository = ProductRepository(productDao)


    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val allProducts: StateFlow<List<Product>> =
        productRepository.observeProducts()
            .map { list -> list.map { it.toProductModel() } } // convierte ProductEntity a Product
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    val products: StateFlow<List<Product>> =
        combine(allProducts, _searchQuery, _selectedCategory) { list, query, cat ->
            list.filter { query.isBlank() || it.name.contains(query, true) || it.description.contains(query, true) }
                .filter { cat == null || it.category.equals(cat, true) }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            productRepository.refreshProducts()
        }
    }

    fun onSearchQueryChange(q: String) {
        _searchQuery.value = q
    }

    fun selectCategory(name: String) {
        _selectedCategory.value = if (_selectedCategory.value == name) null else name
    }
}
