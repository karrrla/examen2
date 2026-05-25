package com.escom.examenfirebase.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.escom.examenfirebase.databinding.ItemNotificationBinding
import com.escom.examenfirebase.model.NotificationItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.VH>() {

    private val items = mutableListOf<NotificationItem>()
    private val df = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    fun setItems(list: List<NotificationItem>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(val b: ItemNotificationBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemNotificationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.b.tvTitle.text = item.title
        holder.b.tvBody.text = item.body
        holder.b.tvDate.text = df.format(Date(item.timestamp))
    }

    override fun getItemCount(): Int = items.size
}
