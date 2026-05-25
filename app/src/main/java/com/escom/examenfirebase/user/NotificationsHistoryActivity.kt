package com.escom.examenfirebase.user

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.escom.examenfirebase.databinding.ActivityNotificationsHistoryBinding
import com.escom.examenfirebase.model.NotificationItem
import com.escom.examenfirebase.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query

class NotificationsHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationsHistoryBinding
    private val adapter = NotificationsAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationsHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Historial de Notificaciones"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        loadNotifications()
    }

    private fun loadNotifications() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
        FirebaseFirestore.getInstance()
            .collection(Constants.NOTIFICATIONS_COLLECTION)
            .whereEqualTo("recipientUid", uid)
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, _ ->
                val list = mutableListOf<NotificationItem>()
                snapshot?.documents?.forEach { d ->
                    d.toObject(NotificationItem::class.java)?.let {
                        it.id = d.id
                        list.add(it)
                    }
                }
                adapter.setItems(list)
                binding.tvEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            }
    }
}
