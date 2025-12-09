package com.example.huertohogar.repository

import com.example.huertohogar.data.db.ProductDao
import com.example.huertohogar.data.db.ProductEntity
import com.example.huertohogar.data.model.Product
import com.example.huertohogar.data.network.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class ProductRepository(
    private val dao: ProductDao
) {

    fun observeProducts(): Flow<List<ProductEntity>> = dao.getAllProducts()

    suspend fun refreshProducts() {
        try {
            // Llamada a la API, devuelve List<ProductEntity>
            val remote: List<ProductEntity> = RetrofitInstance.api.getProducts()

            // Limpiar la tabla actual
            dao.clearAll()

            // Insertar directamente los ProductEntity recibidos
            dao.insertAll(remote)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}
