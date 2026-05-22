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
import com.example.collagealert.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().reference

        if (auth.currentUser == null) {
            navigateToLogin()
            return
        }

        setupNavigation()
        updateFCMToken()
        requestNotificationPermission()
        startNotificationListener()

        if (savedInstanceState == null) {
            replaceFragment(AlertsFragment())
        }
    }

    private fun setupNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_alerts -> {
                    replaceFragment(AlertsFragment())
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
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun updateFCMToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                auth.currentUser?.uid?.let { uid ->
                    database.child("users").child(uid).child("fcmToken").setValue(task.result)
                }
            }
        }
    }

    private fun startNotificationListener() {
        val uid = auth.currentUser?.uid ?: return
        val startTime = System.currentTimeMillis() - 5000

        database.child("users").child(uid).child("notifications")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val notif = snapshot.getValue(AppNotification::class.java)
                    if (notif != null && notif.timestamp > startTime) {
                        NotificationHelper.showLocalNotification(this@MainActivity, notif.title, notif.message)
                    }
                }
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onCancelled(error: DatabaseError) {}
            })

        database.child("global_notifications")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    val notif = snapshot.getValue(AppNotification::class.java)
                    if (notif != null && notif.timestamp > startTime) {
                        NotificationHelper.showLocalNotification(this@MainActivity, notif.title, notif.message)
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

    override fun onStart() {
        super.onStart()
        if (auth.currentUser == null) {
            navigateToLogin()
        }
    }
}