package com.example.dogdatabase.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.example.dogdatabase.data.database.Dogs
import com.example.dogdatabase.data.holder.ViewHolder
import com.example.dogdatabase.databinding.ItemBinding

class Adapter(
    private val onItemClicked: (Dogs) -> Unit
) : ListAdapter<Dogs, ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBinding.inflate(
                LayoutInflater.from(
                    parent.context
                )
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        holder.bind(current)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Dogs>() {
            override fun areItemsTheSame(
                oldItem: Dogs,
                newItem: Dogs
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Dogs,
                newItem: Dogs
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}