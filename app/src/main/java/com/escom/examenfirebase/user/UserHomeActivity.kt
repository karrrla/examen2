package com.escom.examenfirebase.user

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.escom.examenfirebase.auth.LoginActivity
import com.escom.examenfirebase.databinding.ActivityUserHomeBinding
import com.escom.examenfirebase.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserHomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Inicio"

        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            FirebaseFirestore.getInstance()
                .collection(Constants.USERS_COLLECTION)
                .document(uid)
                .get()
                .addOnSuccessListener { doc ->
                    val name = doc.getString("name") ?: "Usuario"
                    binding.tvWelcome.text = "Bienvenido, $name"
                }
        }

        binding.btnProfile.setOnClickListener {
            startActivity(Intent(this, ProfileActivity::class.java))
        }
        binding.btnNotifHistory.setOnClickListener {
            startActivity(Intent(this, NotificationsHistoryActivity::class.java))
        }
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}
