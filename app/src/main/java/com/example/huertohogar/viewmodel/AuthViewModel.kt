package com.example.huertohogar.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.model.AuthState
import com.example.huertohogar.data.model.LoginRequest
import com.example.huertohogar.data.model.RegisterRequest
import com.example.huertohogar.data.network.RetrofitInstance
import com.example.huertohogar.repository.UserSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionRepo = UserSessionRepository(application)

    val isLoggedIn: StateFlow<Boolean> =
        sessionRepo.isLoggedIn.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            // --- INICIO DEL BYPASS TEMPORAL (SOLUCIÓN DE EMERGENCIA) ---
            // Esto permite entrar SIEMPRE, ignorando si el servidor da error 404
            Log.w("AuthDebug", "⚠️ BYPASS ACTIVO: Forzando login exitoso para pruebas.")

            // Simulamos una pequeña espera para que se vea el indicador de carga
            kotlinx.coroutines.delay(1000)

            sessionRepo.setLoggedIn(true)
            _authState.value = AuthState.Success
            return@launch
            // --- FIN DEL BYPASS ---

            /* CÓDIGO REAL (COMENTADO HASTA QUE SE ARREGLE LA URL DEL SERVIDOR)
            val cleanEmail = email.trim()
            val cleanPass = pass.trim()

            Log.d("AuthDebug", "Intentando login con: '$cleanEmail' y pass: '$cleanPass'")

            try {
                val response = RetrofitInstance.api.login(LoginRequest(cleanEmail, cleanPass))

                Log.d("AuthDebug", "Login exitoso. Token: ${response.token}")
                sessionRepo.setLoggedIn(true)
                _authState.value = AuthState.Success

            } catch (e: HttpException) {
                Log.e("AuthDebug", "Error HTTP: Código ${e.code()}. Mensaje: ${e.message()}")

                val errorMsg = if (e.code() == 404 || e.code() == 401) {
                    "Usuario no encontrado o contraseña incorrecta"
                } else {
                    "Error del servidor: ${e.code()}"
                }
                _authState.value = AuthState.Error(errorMsg)

            } catch (e: IOException) {
                Log.e("AuthDebug", "Error de Red: ${e.message}")
                _authState.value = AuthState.Error("Sin conexión a internet")
            } catch (e: Exception) {
                Log.e("AuthDebug", "Error Desconocido: ${e.message}")
                _authState.value = AuthState.Error("Error: ${e.message}")
            }
            */
        }
    }

    fun register(nombre: String, email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            val cleanNombre = nombre.trim()
            val cleanEmail = email.trim()
            val cleanPass = pass.trim()

            // --- BYPASS TAMBIÉN PARA REGISTRO ---
            // Así te aseguras que al registrarte también te deje pasar
            Log.w("AuthDebug", "⚠️ BYPASS ACTIVO: Forzando registro exitoso.")
            kotlinx.coroutines.delay(1000)
            _authState.value = AuthState.Success
            return@launch

            /* CÓDIGO REAL DE REGISTRO
            try {
                val response = RetrofitInstance.api.register(RegisterRequest(cleanNombre, cleanEmail, cleanPass))

                if (response.isSuccessful) {
                    Log.d("AuthDebug", "Registro exitoso: Código ${response.code()}")
                    _authState.value = AuthState.Success
                } else {
                    Log.e("AuthDebug", "Error Registro: ${response.code()}")
                    _authState.value = AuthState.Error("Error registro: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AuthDebug", "Excepción Registro: ${e.message}")
                _authState.value = AuthState.Error("Fallo al registrar: ${e.message}")
            }
            */
        }
    }

    fun resetState() {
        _authState.value = AuthState.Idle
    }

    fun logout() {
        viewModelScope.launch {
            sessionRepo.setLoggedIn(false)
            _authState.value = AuthState.Idle
        }
    }
}