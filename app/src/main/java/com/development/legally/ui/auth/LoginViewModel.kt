package com.development.legally.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.development.legally.ui.ClasesSupremas.UserSession
class LoginViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    sealed class LoginState {
        object Idle : LoginState()
        object Loading : LoginState()
        object Success : LoginState()
        data class Error(val message: String) : LoginState()
    }

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        if (email.isBlank() || password.isBlank()) {
            _loginState.value = LoginState.Error("Por favor complete todos los campos")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _loginState.value = LoginState.Error("Ingrese un correo válido")
            return
        }
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            android.util.Log.d("LoginDebug", "Intentando login con: $email")
            val result = authRepository.login(email, password)
            android.util.Log.d("LoginDebug", "Resultado: ${result.isSuccess}, Error: ${result.exceptionOrNull()?.message}")
            if (result.isSuccess) {
                UserSession.currentUser = result.getOrNull()
                _loginState.value = LoginState.Success
            } else {
                _loginState.value = LoginState.Error(
                    result.exceptionOrNull()?.message ?: "Error al iniciar sesión"
                )
            }
        }
    }

    fun resetState() {
        _loginState.value = LoginState.Idle
    }
}