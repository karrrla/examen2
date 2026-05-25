package com.escom.examenfirebase.admin

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.escom.examenfirebase.databinding.ItemUserSelectableBinding
import com.escom.examenfirebase.model.User

class SelectableUsersAdapter : RecyclerView.Adapter<SelectableUsersAdapter.VH>() {

    private val items = mutableListOf<User>()
    private val selectedUids = mutableSetOf<String>()

    fun setItems(list: List<User>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun getSelectedUsers(): List<User> = items.filter { selectedUids.contains(it.uid) }

    fun clearSelection() {
        selectedUids.clear()
        notifyDataSetChanged()
    }

    inner class VH(val b: ItemUserSelectableBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val b = ItemUserSelectableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(b)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val user = items[position]
        holder.b.tvName.text = user.name
        holder.b.tvEmail.text = user.email
        holder.b.tvRole.text = if (user.role == "admin") "Administrador" else "Usuario"
        holder.b.cbSelect.setOnCheckedChangeListener(null)
        holder.b.cbSelect.isChecked = selectedUids.contains(user.uid)
        holder.b.cbSelect.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) selectedUids.add(user.uid) else selectedUids.remove(user.uid)
        }
        holder.b.root.setOnClickListener {
            holder.b.cbSelect.isChecked = !holder.b.cbSelect.isChecked
        }
    }

    override fun getItemCount(): Int = items.size
}
