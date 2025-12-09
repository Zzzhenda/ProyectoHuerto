package com.example.huertohogar.data.model

// --- Requests (Lo que enviamos) ---
data class LoginRequest(
    val email: String,
    val password: String
)

data class RegisterRequest(
    val nombre: String,
    val email: String,
    val password: String
)

// --- Responses (Lo que recibimos) ---
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

// --- Estados de la UI (Para controlar Carga y Errores) ---
sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Success : AuthState()
    data class Error(val message: String) : AuthState()
}