package com.escom.examenfirebase.admin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.escom.examenfirebase.databinding.ActivityUsersListBinding
import com.escom.examenfirebase.model.User
import com.escom.examenfirebase.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore

class UsersListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUsersListBinding
    private val adapter = UsersAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUsersListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Usuarios Registrados"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        loadUsers()
    }

    private fun loadUsers() {
        FirebaseFirestore.getInstance()
            .collection(Constants.USERS_COLLECTION)
            .addSnapshotListener { snapshot, _ ->
                val list = mutableListOf<User>()
                snapshot?.documents?.forEach { d ->
                    d.toObject(User::class.java)?.let { list.add(it) }
                }
                adapter.setItems(list)
            }
    }
}
