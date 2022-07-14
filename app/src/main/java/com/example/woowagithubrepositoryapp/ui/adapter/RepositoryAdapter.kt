package com.example.woowagithubrepositoryapp.ui.adapter

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ItemRepoBinding
import com.example.woowagithubrepositoryapp.model.Repo

class RepositoryAdapter(private val context : Context) : ListAdapter<Repo,RepositoryAdapter.RepoViewHolder>(RepoDiffCallback()){

    inner class RepoViewHolder(
        private val binding: ItemRepoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Repo) {
            binding.repo = item
            binding.repoLanguageImageView.backgroundTintList = ColorStateList.valueOf(item.languageColor)
            Glide.with(context)
                .load(item.owner.avatarUrl)
                .transform(CircleCrop())
                .into(binding.repoOwnerAvatarImageView)
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