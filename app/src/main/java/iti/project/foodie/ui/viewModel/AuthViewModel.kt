package iti.project.foodie.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import iti.project.foodie.data.repository.AuthRepository
import iti.project.foodie.data.source.local.User
import kotlinx.coroutines.launch
import java.util.Date

class AuthViewModel(private val authRepository: AuthRepository) : ViewModel() {

    fun insertUser(user: User, oncomlete: () -> Unit) {
        viewModelScope.launch {
            authRepository.insertUser(user)
            oncomlete()
        }
    }

    fun login(email: String, password: String, onResult: (User?) -> Unit) {
        viewModelScope.launch {
            val user = authRepository.login(email, password)
            onResult(user)
        }
    }




    fun deleteUser(email: String) {
        viewModelScope.launch {
            authRepository.deleteUser(email)

        }
    }

    fun getCurrentUserId(email: String){
        viewModelScope.launch {
            authRepository.getCurrentUserId(email)
        }
    }
}