package com.development.legally.ui.clients

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.development.legally.R
import com.development.legally.data.model.Client

class ClientAdapter(private val onClick: (Client) -> Unit) : ListAdapter<Client, ClientAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<Client>() {
            override fun areItemsTheSame(oldItem: Client, newItem: Client) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: Client, newItem: Client) = oldItem == newItem
        }
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_client_name)
        val sub: TextView = itemView.findViewById(R.id.tv_client_sub)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_client, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val client = getItem(position)
        holder.name.text = "${client.name} ${client.lastName}".trim()
        holder.sub.text = client.phone ?: client.email ?: ""
        holder.itemView.setOnClickListener { onClick(client) }
    }
}
