package com.example.collagealert

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance().getReference("users")

    private val _user = MutableLiveData<FirebaseUser?>()
    val user: LiveData<FirebaseUser?> = _user

    private val _userRole = MutableLiveData<String?>()
    val userRole: LiveData<String?> = _userRole

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _updateSuccess = MutableLiveData<String?>()
    val updateSuccess: LiveData<String?> = _updateSuccess

    private val _needsEmailVerification = MutableLiveData<Boolean>()
    val needsEmailVerification: LiveData<Boolean> = _needsEmailVerification

    private val ADMIN_EMAIL = "kidstekinfe21@gmail.com"

    fun checkExistingSession() {
        val currentUser = auth.currentUser ?: return
        _isLoading.value = true
        currentUser.reload().addOnCompleteListener {
            val email = currentUser.email?.lowercase()?.trim()
            val isAdmin = email == ADMIN_EMAIL.lowercase()
            if (currentUser.isEmailVerified || isAdmin) {
                Log.d("AUTH", "Existing session found → fetching role")
                _user.value = currentUser
                fetchUserRole()
            } else {
                Log.d("AUTH", "Existing session but email not verified")
                _needsEmailVerification.value = true
                _isLoading.value = false
            }
        }
    }

    fun login(email: String, password: String) {
        _isLoading.value = true
        _error.value = null
        _needsEmailVerification.value = false

        viewModelScope.launch {
            try {
                val cleanedEmail = email.trim()
                val result = auth.signInWithEmailAndPassword(cleanedEmail, password).await()
                val firebaseUser = result.user

                if (firebaseUser != null) {
                    val userEmail = firebaseUser.email?.lowercase()?.trim()
                    val isAdmin = userEmail == ADMIN_EMAIL.lowercase()
                    Log.d("AUTH", "Login → email='$userEmail', isVerified=${firebaseUser.isEmailVerified}, isAdmin=$isAdmin")

                    if (firebaseUser.isEmailVerified || isAdmin) {
                        _user.value = firebaseUser
                        delay(300)
                        fetchUserRole()
                    } else {
                        _needsEmailVerification.value = true
                        _error.value = "Please verify your email. Check your inbox and Spam folder."
                        _isLoading.value = false
                    }
                } else {
                    _error.value = "Login failed: No user returned"
                    _isLoading.value = false
                }
            } catch (e: FirebaseAuthException) {
                _error.value = when (e.errorCode) {
                    "ERROR_INVALID_CREDENTIAL", "ERROR_WRONG_PASSWORD", "ERROR_USER_NOT_FOUND" ->
                        "Invalid email or password."
                    "ERROR_INVALID_EMAIL" -> "Invalid email format."
                    "ERROR_TOO_MANY_REQUESTS" ->
                        "Too many failed attempts. Account temporarily locked. Try again later or reset your password."
                    "ERROR_USER_DISABLED" ->
                        "This account has been disabled. Contact support."
                    "ERROR_NETWORK_REQUEST_FAILED" ->
                        "No internet connection. Please check your network."
                    else -> "Login failed: ${e.message}"
                }
                _isLoading.value = false
            } catch (e: Exception) {
                _error.value = "Error: ${e.message}"
                _isLoading.value = false
            }
        }
    }

    fun signUp(email: String, password: String, name: String, role: String) {
        _isLoading.value = true
        _error.value = null
        _needsEmailVerification.value = false

        viewModelScope.launch {
            try {
                val cleanedEmail = email.trim()
                val result = auth.createUserWithEmailAndPassword(cleanedEmail, password).await()
                val firebaseUser = result.user ?: throw Exception("User creation failed")

                val finalRole = if (cleanedEmail.lowercase() == ADMIN_EMAIL.lowercase()) "admin" else role
                val userData = User(uid = firebaseUser.uid, email = cleanedEmail, name = name, role = finalRole)
                database.child(firebaseUser.uid).setValue(userData).await()

                firebaseUser.sendEmailVerification().await()

                _needsEmailVerification.value = true
                _updateSuccess.value = "Account created! Verification email sent to $cleanedEmail. Check your Spam folder too."
            } catch (e: FirebaseAuthException) {
                _error.value = when (e.errorCode) {
                    "ERROR_EMAIL_ALREADY_IN_USE" -> "Email already in use."
                    "ERROR_WEAK_PASSWORD" -> "Password is too weak (min 6 characters)."
                    else -> "Sign up failed: ${e.message}"
                }
            } catch (e: Exception) {
                _error.value = "Sign up failed: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun resendVerificationEmail() {
        val currentUser = auth.currentUser ?: run {
            _error.value = "Please log in first, then tap Resend."
            return
        }
        if (currentUser.isEmailVerified) {
            _error.value = "Email is already verified. Please log in normally."
            return
        }
        _isLoading.value = true
        currentUser.sendEmailVerification()
            .addOnSuccessListener {
                _isLoading.value = false
                _updateSuccess.value = "✅ Verification link resent to ${currentUser.email}. Check Inbox and Spam."
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                _error.value = "Failed to resend: ${e.message}"
            }
    }

    fun resetPassword(email: String) {
        if (email.isEmpty()) {
            _error.value = "Enter your email address first"
            return
        }
        _isLoading.value = true
        _error.value = null
        val cleanEmail = email.trim()
        Log.d("AUTH", "Sending password reset to: $cleanEmail")
        auth.sendPasswordResetEmail(cleanEmail)
            .addOnSuccessListener {
                _isLoading.value = false
                Log.d("AUTH", "✅ Reset email sent to $cleanEmail")
                _updateSuccess.value = "✅ Password reset email sent to $cleanEmail. Check Inbox and Spam."
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                Log.e("AUTH", "❌ Reset failed: ${e.message}")
                _error.value = when {
                    e.message?.contains("no user record") == true ||
                    e.message?.contains("USER_NOT_FOUND") == true ->
                        "No account found with that email. Please register first."
                    else -> "Failed to send reset email: ${e.message}"
                }
            }
    }

    private fun fetchUserRole() {
        val currentUser = auth.currentUser
        val uid = currentUser?.uid ?: return
        val email = currentUser.email?.lowercase()?.trim()

        Log.d("AUTH", "fetchUserRole → uid=$uid, email='$email'")

        if (email == ADMIN_EMAIL.lowercase()) {
            Log.d("AUTH", "✅ Admin email confirmed → role = admin")
            _userRole.value = "admin"
            _isLoading.value = false
            database.child(uid).child("role").setValue("admin")
            return
        }

        database.child(uid).child("role").get()
            .addOnSuccessListener { snapshot ->
                val role = snapshot.getValue(String::class.java)
                Log.d("AUTH", "✅ Role from DB = '$role'")
                if (!role.isNullOrEmpty()) {
                    _userRole.value = role
                } else {
                    database.child(uid).child("role").setValue("student")
                    _userRole.value = "student"
                }
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                Log.e("AUTH", "❌ Failed to fetch role: ${e.message}")
                _error.value = "Failed to fetch user role: ${e.message}"
                _isLoading.value = false
            }
    }

    fun updateUserName(newName: String) {
        val uid = auth.currentUser?.uid ?: return
        _isLoading.value = true
        database.child(uid).child("name").setValue(newName)
            .addOnSuccessListener {
                _isLoading.value = false
                _updateSuccess.value = "Username updated successfully"
            }
            .addOnFailureListener {
                _isLoading.value = false
                _error.value = "Failed to update username"
            }
    }

    fun updatePassword(newPassword: String) {
        val currentUser = auth.currentUser ?: return
        _isLoading.value = true
        currentUser.updatePassword(newPassword)
            .addOnSuccessListener {
                _isLoading.value = false
                _updateSuccess.value = "Password updated successfully"
            }
            .addOnFailureListener { e ->
                _isLoading.value = false
                if (e is FirebaseAuthException && e.errorCode == "ERROR_REQUIRES_RECENT_LOGIN") {
                    _error.value = "Security timeout. Please logout and login again."
                } else {
                    _error.value = "Failed to update password: ${e.message}"
                }
            }
    }

    fun logout() {
        auth.signOut()
        _user.value = null
        _userRole.value = null
        _needsEmailVerification.value = false
    }

    fun clearError() { _error.value = null }
    fun clearUpdateSuccess() { _updateSuccess.value = null }
}
