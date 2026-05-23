package com.example.collagealert

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.collagealert.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth
    private lateinit var authViewModel: AuthViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        authViewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        sharedPreferences = requireContext().getSharedPreferences("Settings", Context.MODE_PRIVATE)

        setupProfile()
        setupLogout()
        setupThemeToggle()
        setupEditName()
        setupChangePassword()
        setupObservers()
    }

    private fun setupProfile() {
        val user = auth.currentUser ?: return
        binding.profileEmail.text = user.email ?: ""

        val uid = user.uid
        val db = FirebaseDatabase.getInstance().getReference("users").child(uid)

        db.child("name").get().addOnSuccessListener { snapshot ->
            val name = snapshot.getValue(String::class.java)
            binding.profileName.text = name
                ?: user.email?.substringBefore("@")?.replaceFirstChar { it.uppercase() }
                ?: "Student"
        }
    }

    private fun setupObservers() {
        authViewModel.updateSuccess.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
                authViewModel.clearUpdateSuccess()
                if (it.contains("Username")) setupProfile()
            }
        }

        authViewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
                authViewModel.clearError()
            }
        }
    }

    private fun setupEditName() {
        binding.btnEditName.setOnClickListener {
            val input = EditText(requireContext())
            input.hint = "Enter new username"
            input.setText(binding.profileName.text)

            val container = LinearLayout(requireContext())
            container.orientation = LinearLayout.VERTICAL
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(60, 20, 60, 0)
            input.layoutParams = params
            container.addView(input)

            AlertDialog.Builder(requireContext())
                .setTitle("Change Username")
                .setView(container)
                .setPositiveButton("Update") { _, _ ->
                    val newName = input.text.toString().trim()
                    if (newName.isNotEmpty()) {
                        authViewModel.updateUserName(newName)
                    } else {
                        Toast.makeText(requireContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupChangePassword() {
        binding.btnChangePassword.setOnClickListener {
            val input = EditText(requireContext())
            input.hint = "Enter new password"
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD

            val container = LinearLayout(requireContext())
            container.orientation = LinearLayout.VERTICAL
            val params = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            params.setMargins(60, 20, 60, 0)
            input.layoutParams = params
            container.addView(input)

            AlertDialog.Builder(requireContext())
                .setTitle("Change Password")
                .setMessage("Enter your new password (min 6 characters)")
                .setView(container)
                .setPositiveButton("Update") { _, _ ->
                    val newPassword = input.text.toString()
                    if (newPassword.length >= 6) {
                        authViewModel.updatePassword(newPassword)
                    } else {
                        Toast.makeText(requireContext(), "Password too short", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun setupThemeToggle() {
        val isDarkMode = sharedPreferences.getBoolean("dark_mode", false)
        binding.switchDarkMode.isChecked = isDarkMode

        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            sharedPreferences.edit().putBoolean("dark_mode", isChecked).apply()
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }

    private fun setupLogout() {
        binding.logoutButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("Logout")
                .setMessage("Are you sure you want to logout?")
                .setPositiveButton("Yes") { _, _ ->
                    auth.signOut()
                    Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                    navigateToLogin()
                }
                .setNegativeButton("No", null)
                .show()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
