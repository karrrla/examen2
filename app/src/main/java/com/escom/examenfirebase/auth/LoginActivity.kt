package com.escom.examenfirebase.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.escom.examenfirebase.admin.AdminHomeActivity
import com.escom.examenfirebase.databinding.ActivityLoginBinding
import com.escom.examenfirebase.user.UserHomeActivity
import com.escom.examenfirebase.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnLogin.setOnClickListener { doLogin() }
        binding.tvGoRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun doLogin() {
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                updateFcmToken(uid)
                fetchRoleAndNavigate(uid)
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun updateFcmToken(uid: String) {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            db.collection(Constants.USERS_COLLECTION)
                .document(uid)
                .update("fcmToken", token)
        }
    }

    private fun fetchRoleAndNavigate(uid: String) {
        db.collection(Constants.USERS_COLLECTION).document(uid).get()
            .addOnSuccessListener { doc ->
                binding.progressBar.visibility = View.GONE
                val role = doc.getString("role") ?: Constants.ROLE_USER
                val intent = if (role == Constants.ROLE_ADMIN) {
                    Intent(this, AdminHomeActivity::class.java)
                } else {
                    Intent(this, UserHomeActivity::class.java)
                }
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "No se pudo leer el perfil", Toast.LENGTH_SHORT).show()
            }
    }
}
