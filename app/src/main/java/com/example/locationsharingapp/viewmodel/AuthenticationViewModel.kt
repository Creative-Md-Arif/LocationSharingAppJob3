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

    // reset password function

//    fun resetPassword(email: String, onSuccess: () -> Unit, onFailure: (String) -> Unit) {
//        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener {
//                if (it.isSuccessful) {
//                    onSuccess()
//                } else {
//                    onFailure(it.exception?.message ?: "Password Reset Failed")
//
//                }
//            }
//    }

// email verification function

//    fun emailVerification(onSuccess: () -> Unit, onFailure: (String) -> Unit) {
//        val user = firebaseAuth.currentUser
//        user?.sendEmailVerification()
//
//            ?.addOnCompleteListener {
//                if (it.isSuccessful) {
//                    onSuccess()
//                } else {
//                    onFailure(it.exception?.message ?: "Email Verification Failed")
//
//                }
//
//            }
//
//    }

//    email verification check function

//    fun isEmailVerified(): Boolean {
//        val user = firebaseAuth.currentUser
//        return user?.isEmailVerified == true
//    }

//  firebase logout function


    fun getCurrentUserId(): String {
        return firebaseAuth.currentUser?.uid ?: ""
    }

    //    firebase login check function
    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    //    firebase logout function
    fun getCurrentUser(): FirebaseUser? {
        return firebaseAuth.currentUser
    }


}