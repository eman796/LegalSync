package com.development.legally.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.development.legally.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val authRepository = AuthRepository()

    sealed class RegistrationState {
        object Idle : RegistrationState()
        object Loading : RegistrationState()
        object Success : RegistrationState()
        data class Error(val message: String) : RegistrationState()
    }

    private val _registrationState = MutableStateFlow<RegistrationState>(RegistrationState.Idle)
    val registrationState: StateFlow<RegistrationState> = _registrationState

    fun requestRegistration(fullName: String, email: String) {
        if (fullName.isBlank() || email.isBlank()) {
            _registrationState.value = RegistrationState.Error("Por favor complete todos los campos")
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _registrationState.value = RegistrationState.Error("Ingrese un correo válido")
            return
        }

        viewModelScope.launch {
            _registrationState.value = RegistrationState.Loading
            val result = authRepository.requestRegistration(fullName, email, password = "ElizabethGrantDarkParadise")
            if (result.isSuccess) {
                _registrationState.value = RegistrationState.Success
            } else {
                _registrationState.value = RegistrationState.Error(
                    result.exceptionOrNull()?.message ?: "Error al enviar la solicitud"
                )
            }
        }
    }

    fun resetState() {
        _registrationState.value = RegistrationState.Idle
    }
}