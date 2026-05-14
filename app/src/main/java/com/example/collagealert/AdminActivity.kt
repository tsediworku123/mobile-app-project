package com.example.collagealert

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.collagealert.databinding.ActivityAdminBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class AdminActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAdminBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        checkAdminStatus()
        setupNavigation()
        requestNotificationPermission()
        startNotificationListener()

        // Set default fragment
        if (savedInstanceState == null) {
            replaceFragment(AdminAlertsFragment())
        }
    }

    private fun setupNavigation() {
        binding.adminBottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_alerts -> {
                    replaceFragment(AdminAlertsFragment())
                    true
                }
                R.id.navigation_news -> {
                    replaceFragment(NewsFragment())
                    true
                }
                R.id.navigation_notifications -> {
                    replaceFragment(NotificationFragment())
                    true
                }
                R.id.navigation_settings -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.admin_nav_host_fragment, fragment)
            .commit()
    }

    private fun checkAdminStatus() {
        val uid = auth.currentUser?.uid ?: return navigateToLogin()
        database.child("users").child(uid).child("role").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.getValue(String::class.java) != "admin") {
                    navigateToMain()
                }
            }
    }

    fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun startNotificationListener() {
        val uid = auth.currentUser?.uid ?: return
        val startTime = System.currentTimeMillis() - 5000 // 5-second buffer to catch recent changes

        // Listen for personal notifications
        database.child("users").child(uid).child("notifications")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val notif = snapshot.getValue(AppNotification::class.java)
                    if (notif != null && notif.timestamp > startTime) {
                        NotificationHelper.showLocalNotification(this@AdminActivity, notif.title, notif.message)
                    }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })

        // Listen for global notifications
        database.child("global_notifications")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val notif = snapshot.getValue(AppNotification::class.java)
                    if (notif != null && notif.timestamp > startTime) {
                        NotificationHelper.showLocalNotification(this@AdminActivity, notif.title, notif.message)
                    }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                    if (!isGranted) {
                        Toast.makeText(this, "Notifications disabled. You might miss important updates.", Toast.LENGTH_LONG).show()
                    }
                }.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}