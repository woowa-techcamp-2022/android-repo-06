package com.example.woowagithubrepositoryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.databinding.ItemNotificationBinding
import com.example.woowagithubrepositoryapp.model.Notification

class NotificationAdapter : ListAdapter<Notification, NotificationAdapter.NotificationViewHolder>(NotificationDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class NotificationViewHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Notification) {
            binding.apply {
                this.notification = item
                executePendingBindings()
            }
        }
    }

    private class NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.subject.title == newItem.subject.title
        }
    }
}