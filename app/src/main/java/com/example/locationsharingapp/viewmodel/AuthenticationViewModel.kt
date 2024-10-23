package com.example.locationsharingapp.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthenticationViewModel : ViewModel() {

    private val firebaseAuth = FirebaseAuth.getInstance()

    //login function
    fun login(email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(it.exception?.message ?: "Login Failed")
                }
            }

    }

    // register function
    fun register(
        email: String, password: String, onSuccess: () -> Unit, onFailure: (String) -> Unit
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFailure(it.exception?.message ?: "Registration Failed")

                }
            }

    }




    fun getCurrentUserId(): String {
        return firebaseAuth.currentUser?.uid ?: ""
    }


    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }


    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }


}