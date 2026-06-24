package com.development.legally.ui.followups

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.development.legally.R
import com.development.legally.data.model.FollowUp

class FollowUpAdapter : ListAdapter<FollowUp, FollowUpAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<FollowUp>() {
            override fun areItemsTheSame(oldItem: FollowUp, newItem: FollowUp) = oldItem.id == newItem.id
            override fun areContentsTheSame(oldItem: FollowUp, newItem: FollowUp) = oldItem == newItem
        }
    }

    inner class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val date: TextView = itemView.findViewById(R.id.tv_followup_date)
        val desc: TextView = itemView.findViewById(R.id.tv_followup_desc)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_followup, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val f = getItem(position)
        holder.date.text = f.date
        holder.desc.text = f.description
    }
}
