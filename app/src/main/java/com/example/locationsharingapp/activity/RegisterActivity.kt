package com.example.locationsharingapp.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.locationsharingapp.R
import com.example.locationsharingapp.databinding.ActivityRegisterBinding
import com.example.locationsharingapp.viewmodel.AuthenticationViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var authenticationViewModel: AuthenticationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)



        binding.registerBtn.setOnClickListener {
            val username = binding.displayNameEt.text.toString().trim() // Capture username
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()
            val confirmPassword = binding.confirmPasswordEt.text.toString().trim()  // Capture confirm password

            // Validate email, password, and confirm password
            if (validateInput(username, email, password, confirmPassword)) {


                // Proceed with registration if validation is successful
                authenticationViewModel.register(email, password, {
                    // Hide the loading dialog when registration is successful


                    // After successful registration, send email verification
                    authenticationViewModel.emailVerification(
                        onSuccess = {
                            Toast.makeText(this, "Verification email sent!", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, LoginActivity::class.java))
                            finish() // Close the RegisterActivity
                        },
                        onFailure = {
                            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                        }
                    )
                }, { errorMessage ->

                    // Handle specific error case for email already in use
                    if (errorMessage.contains("email address is already in use", ignoreCase = true)) {
                        Toast.makeText(this, "This email is already registered. Please use a different email.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }

        binding.loginTxt.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish() // Close the RegisterActivity
        }
    }

//    override fun onStart() {
//        super.onStart()
//        if (Firebase.auth.currentUser != null) {
//            startActivity(Intent(this@RegisterActivity, MainActivity::class.java))
//            finish()
//        }
//    }

    // Input validation function
    private fun validateInput(
        username: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        // Check if username is empty
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if email is empty
        if (email.isEmpty()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if email is in a valid format
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if password is empty
        if (password.isEmpty()) {
            Toast.makeText(this, "Please enter a password", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if password meets minimum requirements (e.g., at least 6 characters)
        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        // Check if password and confirm password match
        if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true // All validations passed
    }
}
