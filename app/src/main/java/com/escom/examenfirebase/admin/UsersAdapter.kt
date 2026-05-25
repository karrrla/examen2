package com.escom.examenfirebase.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.escom.examenfirebase.databinding.ItemUserBinding
import com.escom.examenfirebase.model.User

class UsersAdapter : RecyclerView.Adapter<UsersAdapter.VH>() {

    private val items = mutableListOf<User>()

    fun setItems(list: List<User>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    inner class VH(val b: ItemUserBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val user = items[position]
        holder.b.tvName.text = user.name
        holder.b.tvEmail.text = user.email
        holder.b.tvRole.text = if (user.role == "admin") "Administrador" else "Usuario"
    }

    override fun getItemCount(): Int = items.size
}
