package com.example.locationsharingapp.activity

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.locationsharingapp.R
import com.example.locationsharingapp.databinding.ActivityLoginBinding
import com.example.locationsharingapp.viewmodel.AuthenticationViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authenticationViewModel: AuthenticationViewModel
    private lateinit var loadingDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationViewModel = ViewModelProvider(this).get(AuthenticationViewModel::class.java)

        // Initialize the custom loading dialog
        loadingDialog = createLoadingDialog()

        // Check if user is already logged in
        if (Firebase.auth.currentUser != null) {
            // Check if the email is verified before navigating to MainActivity
            if (authenticationViewModel.isEmailVerified()) {
                startActivity(Intent(this, MainActivity::class.java))
                finish() // Close LoginActivity
            } else {
                Toast.makeText(this, "Please verify your email before logging in", Toast.LENGTH_LONG).show()
            }
        }

        binding.loginBtn.setOnClickListener {
            val email = binding.emailEt.text.toString().trim()
            val password = binding.passwordEt.text.toString().trim()

            // Check if email or password is empty
            if (email.isEmpty()) {
                Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                Toast.makeText(this, "Please enter your password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Show loading dialog while attempting login
            showLoadingDialog()

            authenticationViewModel.login(email, password, {
                // Hide the loading dialog when login is successful
                hideLoadingDialog()

                // Check if email is verified
                if (authenticationViewModel.isEmailVerified()) {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish() // Close LoginActivity
                } else {
                    Toast.makeText(this, "Please verify your email before logging in", Toast.LENGTH_LONG).show()
                }
            }, {
                // Hide the loading dialog on failure
                hideLoadingDialog()

                // Show error message
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            })
        }

        // "Create Account" TextView click listener
        binding.registerTxt.setOnClickListener {
            // Navigate to RegistrationActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    // The onStart method can be removed since the check is done in onCreate
    // You can keep it if you prefer
//    override fun onStart() {
//        super.onStart()
//        if (Firebase.auth.currentUser != null) {
//            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
//            finish()
//        }
//    }

    // Create a custom loading dialog
    private fun createLoadingDialog(): Dialog {
        val dialog = Dialog(this)
        val view = LayoutInflater.from(this).inflate(R.layout.loading_dialog, null)
        dialog.setContentView(view)
        dialog.setCancelable(false) // Prevent the user from dismissing the dialog

        // Set dialog width and height
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        return dialog
    }

    // Show the loading dialog
    private fun showLoadingDialog() {
        if (!loadingDialog.isShowing) {
            loadingDialog.show()
        }
    }

    // Hide the loading dialog
    private fun hideLoadingDialog() {
        if (loadingDialog.isShowing) {
            loadingDialog.dismiss()
        }
    }
}
