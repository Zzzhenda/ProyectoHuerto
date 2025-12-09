package com.example.huertohogar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.model.Category
import com.example.huertohogar.data.network.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    init {
        fetchCategories()
    }

    private fun fetchCategories() {
        viewModelScope.launch {
            try {
                // 1. Llamamos a la API (asegúrate de haber actualizado el nombre en ApiServices)
                val allCategories = RetrofitInstance.api.getCategories()

                // 2. FILTRAMOS solo las de "huerto"
                val filteredList = allCategories
                    .filter { it.storeSlug == "huerto" } // Solo tienda huerto
                    .distinctBy { it.name } // Evita que salga "Verduras" dos veces

                _categories.value = filteredList

            } catch (e: Exception) {
                Log.e("CategoryViewModel", "Error: ${e.message}")
                _categories.value = emptyList()
            }
        }
    }
}