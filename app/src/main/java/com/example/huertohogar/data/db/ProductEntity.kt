// ProductEntity.kt
package com.example.huertohogar.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.example.huertohogar.data.model.Product

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,

    @SerializedName("nombre")
    val name: String,

    @SerializedName("precio")
    val price: Double,

    @SerializedName("descripcion")
    val description: String = "",

    @SerializedName("categoria_nombre")
    val category: String = "General",

    @SerializedName("stock")
    val stock: Int,

    @SerializedName("imagen")
    val imageUrl: String = ""
)

fun ProductEntity.toProductModel() = Product(
    id = id.toString(),
    name = name,
    price = price,
    description = description,
    category = category,
    stock = stock,
    imageUrl = imageUrl
)

// Mapper desde JSON remoto a ProductEntity
fun Product.toEntity(): ProductEntity {
    return ProductEntity(
        id = id.toIntOrNull() ?: 0,
        name = name,
        price = price,
        description = description,
        category = category,
        stock = stock,
        imageUrl = imageUrl
    )
}
