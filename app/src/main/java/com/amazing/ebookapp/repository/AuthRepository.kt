package com.amazing.ebookapp.repository

import com.amazing.ebookapp.utils.ResultState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()

    suspend fun signIn(email: String, password: String): ResultState<Boolean> {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            ResultState.Success(data = true)
        } catch (e: Exception) {
            ResultState.Error(error = e.message ?: "Login failed")
        }
    }

    fun signOut(): ResultState<Boolean> {
        return try {
            auth.signOut()
            ResultState.Success(data = true)
        } catch (e: Exception) {
            ResultState.Error(error = e.message ?: "SignOut failed")
        }
    }

}