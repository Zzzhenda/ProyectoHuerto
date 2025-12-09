package com.example.huertohogar.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val id: String,
    val nombre: String,
    val token: String
)

data class UserDto(
    val id: String,
    val email: String
)

data class StockRequest(
    val stock: Int
)
