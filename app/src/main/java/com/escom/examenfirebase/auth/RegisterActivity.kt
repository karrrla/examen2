package com.escom.examenfirebase.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.escom.examenfirebase.R
import com.escom.examenfirebase.admin.AdminHomeActivity
import com.escom.examenfirebase.databinding.ActivityRegisterBinding
import com.escom.examenfirebase.model.User
import com.escom.examenfirebase.user.UserHomeActivity
import com.escom.examenfirebase.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.messaging.FirebaseMessaging

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.swAdmin.setOnCheckedChangeListener { _, isChecked ->
            binding.tilMasterPassword.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        binding.btnRegister.setOnClickListener { doRegister() }
        binding.tvGoLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun doRegister() {
        val name = binding.etName.text.toString().trim()
        val email = binding.etEmail.text.toString().trim()
        val password = binding.etPassword.text.toString()
        val isAdmin = binding.swAdmin.isChecked
        val masterPwd = binding.etMasterPassword.text.toString()

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show()
            return
        }

        if (isAdmin) {
            val expected = getString(R.string.admin_master_password)
            if (masterPwd != expected) {
                Toast.makeText(this, "Contraseña maestra incorrecta", Toast.LENGTH_LONG).show()
                return
            }
        }

        binding.progressBar.visibility = View.VISIBLE
        auth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                val role = if (isAdmin) Constants.ROLE_ADMIN else Constants.ROLE_USER

                FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
                    val user = User(
                        uid = uid,
                        name = name,
                        email = email,
                        role = role,
                        fcmToken = token ?: ""
                    )
                    db.collection(Constants.USERS_COLLECTION).document(uid).set(user)
                        .addOnSuccessListener {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                            val intent = if (role == Constants.ROLE_ADMIN) {
                                Intent(this, AdminHomeActivity::class.java)
                            } else {
                                Intent(this, UserHomeActivity::class.java)
                            }
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this, "Error guardando perfil: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                }
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
