package com.example.collagealert

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.collagealert.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]

        setupObservers()
        setupClickListeners()

        authViewModel.checkExistingSession()
    }

    private fun setupObservers() {
        authViewModel.userRole.observe(this) { role ->
            if (!role.isNullOrEmpty()) {
                when (role) {
                    "admin" -> startActivity(Intent(this, AdminActivity::class.java))
                    else -> startActivity(Intent(this, MainActivity::class.java))
                }
                finish()
            }
        }

        authViewModel.error.observe(this) { error ->
            error?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                authViewModel.clearError()
            }
        }

        authViewModel.updateSuccess.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
                authViewModel.clearUpdateSuccess()
            }
        }

        authViewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.loginButton.isEnabled = !isLoading
        }

        authViewModel.needsEmailVerification.observe(this) { needsVerification ->
            if (needsVerification) {
                binding.resendVerificationButton.visibility = View.VISIBLE
                Toast.makeText(
                    this,
                    "Email not verified. Check your inbox and Spam folder.",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                binding.resendVerificationButton.visibility = View.GONE
            }
        }
    }

    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            authViewModel.login(email, password)
        }

        binding.forgotPasswordLink.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Enter your email address above first", Toast.LENGTH_SHORT).show()
            } else {
                authViewModel.resetPassword(email)
            }
        }

        binding.resendVerificationButton.setOnClickListener {
            authViewModel.resendVerificationEmail()
        }

        binding.signUpLink.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        binding.themeToggle.setOnClickListener {
            toggleTheme()
        }
    }

    private fun toggleTheme() {
        val isDarkMode = AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES
        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }
        recreate()
    }
}
