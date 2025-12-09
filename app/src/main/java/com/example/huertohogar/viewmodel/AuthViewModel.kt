package com.example.huertohogar.viewmodel

// AuthViewModel.kt
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.SharingStarted

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.huertohogar.data.model.LoginRequest
import com.example.huertohogar.data.network.RetrofitInstance
import com.example.huertohogar.repository.UserSessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val sessionRepo = UserSessionRepository(application)

    val isLoggedIn: StateFlow<Boolean> =
        sessionRepo.isLoggedIn.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                RetrofitInstance.api.login(LoginRequest(email, password))
                sessionRepo.setLoggedIn(true)
                onSuccess()
            } catch (_: Exception) { }
        }
    }

    fun register(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            sessionRepo.setLoggedIn(true)
            onSuccess()
        }
    }

    fun logout() {
        viewModelScope.launch {
            sessionRepo.setLoggedIn(false)
        }
    }
}
