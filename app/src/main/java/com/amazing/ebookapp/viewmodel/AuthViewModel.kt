package com.amazing.ebookapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amazing.ebookapp.repository.AuthRepository
import com.amazing.ebookapp.utils.ResultState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val authRepository : AuthRepository) : ViewModel() {

    private val _authState = MutableStateFlow<ResultState<Boolean>?>(null)
    val authState : StateFlow<ResultState<Boolean>?> get() = _authState


    fun signIn(email : String, password : String){
        viewModelScope.launch {
            _authState.value = ResultState.Loading
            val signInResult = authRepository.signIn(email, password)
            _authState.value = signInResult
        }
    }

    fun signOut(){
        _authState.value = authRepository.signOut()
    }

    fun resetAuthState(){
        _authState.value = null
    }


}