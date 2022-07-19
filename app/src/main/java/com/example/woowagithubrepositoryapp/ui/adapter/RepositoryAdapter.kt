package com.example.woowagithubrepositoryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ItemRepoBinding
import com.example.woowagithubrepositoryapp.model.Repo

class RepositoryAdapter() : ListAdapter<Repo,RepositoryAdapter.RepoViewHolder>(RepoDiffCallback()){

    class RepoViewHolder(
        private val binding: ItemRepoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Repo) {
            binding.repo = item
        }
    }

    class RepoDiffCallback : DiffUtil.ItemCallback<Repo>() {
        override fun areItemsTheSame(
            oldItem: Repo,
            newItem: Repo
        ): Boolean = (oldItem.id == newItem.id)

        override fun areContentsTheSame(
            oldItem: Repo,
            newItem: Repo
        ): Boolean = ((oldItem.id == newItem.id) && (oldItem.name == newItem.name))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repo,parent,false)
        val binding = ItemRepoBinding.bind(view)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}