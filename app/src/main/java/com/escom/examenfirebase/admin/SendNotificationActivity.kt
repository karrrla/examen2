package com.escom.examenfirebase.admin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.escom.examenfirebase.databinding.ActivitySendNotificationBinding
import com.escom.examenfirebase.model.NotificationItem
import com.escom.examenfirebase.model.User
import com.escom.examenfirebase.notifications.FcmSender
import com.escom.examenfirebase.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SendNotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySendNotificationBinding
    private val adapter = SelectableUsersAdapter()
    private val allUsers = mutableListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = "Enviar Notificación"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        binding.swSendAll.setOnCheckedChangeListener { _, isChecked ->
            binding.recyclerView.visibility = if (isChecked) View.GONE else View.VISIBLE
            if (isChecked) adapter.clearSelection()
        }

        loadUsers()
        binding.btnSend.setOnClickListener { sendNotification() }
    }

    private fun loadUsers() {
        FirebaseFirestore.getInstance()
            .collection(Constants.USERS_COLLECTION)
            .get()
            .addOnSuccessListener { snapshot ->
                allUsers.clear()
                snapshot.documents.forEach { d ->
                    d.toObject(User::class.java)?.let { allUsers.add(it) }
                }
                adapter.setItems(allUsers)
            }
    }

    private fun sendNotification() {
        val title = binding.etTitle.text.toString().trim()
        val body = binding.etBody.text.toString().trim()

        if (title.isEmpty() || body.isEmpty()) {
            Toast.makeText(this, "Completa título y mensaje", Toast.LENGTH_SHORT).show()
            return
        }

        val recipients = if (binding.swSendAll.isChecked) {
            allUsers
        } else {
            adapter.getSelectedUsers()
        }

        if (recipients.isEmpty()) {
            Toast.makeText(this, "Selecciona al menos un destinatario", Toast.LENGTH_SHORT).show()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        binding.btnSend.isEnabled = false

        val currentUid = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val senderName = allUsers.find { it.uid == currentUid }?.name ?: "Administrador"

        lifecycleScope.launch {
            var ok = 0
            var fail = 0
            withContext(Dispatchers.IO) {
                for (user in recipients) {
                    // 1) Guardar en Firestore (historial)
                    val notif = NotificationItem(
                        title = title,
                        body = body,
                        senderUid = currentUid,
                        senderName = senderName,
                        recipientUid = user.uid,
                        timestamp = System.currentTimeMillis()
                    )
                    try {
                        FirebaseFirestore.getInstance()
                            .collection(Constants.NOTIFICATIONS_COLLECTION)
                            .add(notif)
                    } catch (_: Exception) { }

                    // 2) Enviar push vía FCM HTTP v1
                    if (user.fcmToken.isNotEmpty()) {
                        val success = FcmSender.sendToToken(
                            applicationContext,
                            user.fcmToken,
                            title,
                            body
                        )
                        if (success) ok++ else fail++
                    }
                }
            }
            binding.progressBar.visibility = View.GONE
            binding.btnSend.isEnabled = true
            Toast.makeText(
                this@SendNotificationActivity,
                "Enviadas: $ok | Fallidas: $fail",
                Toast.LENGTH_LONG
            ).show()
            binding.etTitle.setText("")
            binding.etBody.setText("")
            adapter.clearSelection()
        }
    }
}
