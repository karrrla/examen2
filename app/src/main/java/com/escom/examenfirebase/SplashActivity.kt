package com.escom.examenfirebase

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.escom.examenfirebase.admin.AdminHomeActivity
import com.escom.examenfirebase.auth.LoginActivity
import com.escom.examenfirebase.user.UserHomeActivity
import com.escom.examenfirebase.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Solicitar permiso de notificaciones (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    100
                )
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            routeUser()
        }, 1500)
    }

    private fun routeUser() {
        val auth = FirebaseAuth.getInstance()
        val current = auth.currentUser
        if (current == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        // Persistencia: si hay sesión, leemos el rol desde Firestore
        FirebaseFirestore.getInstance()
            .collection(Constants.USERS_COLLECTION)
            .document(current.uid)
            .get()
            .addOnSuccessListener { doc ->
                val role = doc.getString("role") ?: Constants.ROLE_USER
                val intent = if (role == Constants.ROLE_ADMIN) {
                    Intent(this, AdminHomeActivity::class.java)
                } else {
                    Intent(this, UserHomeActivity::class.java)
                }
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                // Si falla, mandamos a login
                auth.signOut()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
    }
}
