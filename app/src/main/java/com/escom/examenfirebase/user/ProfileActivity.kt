package com.escom.examenfirebase.user

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.escom.examenfirebase.databinding.ActivityProfileBinding
import com.escom.examenfirebase.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val db by lazy { FirebaseFirestore.getInstance() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Mi Perfil"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        loadProfile()

        binding.btnSave.setOnClickListener { saveProfile() }
    }

    private fun loadProfile() {
        val uid = auth.currentUser?.uid ?: return
        binding.progressBar.visibility = View.VISIBLE
        db.collection(Constants.USERS_COLLECTION).document(uid).get()
            .addOnSuccessListener { doc ->
                binding.progressBar.visibility = View.GONE
                binding.etName.setText(doc.getString("name") ?: "")
                binding.etEmail.setText(doc.getString("email") ?: "")
                binding.etPhone.setText(doc.getString("phone") ?: "")
                val role = doc.getString("role") ?: "user"
                binding.tvRole.text = "Rol: ${if (role == "admin") "Administrador" else "Usuario"}"
            }
            .addOnFailureListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Error cargando perfil", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveProfile() {
        val uid = auth.currentUser?.uid ?: return
        val name = binding.etName.text.toString().trim()
        val phone = binding.etPhone.text.toString().trim()

        if (name.isEmpty()) {
            Toast.makeText(this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        val updates = mapOf("name" to name, "phone" to phone)
        db.collection(Constants.USERS_COLLECTION).document(uid).update(updates)
            .addOnSuccessListener {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Perfil actualizado", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                binding.progressBar.visibility = View.GONE
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }
}
