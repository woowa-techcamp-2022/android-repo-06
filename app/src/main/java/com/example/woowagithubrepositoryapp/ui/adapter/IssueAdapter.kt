package com.example.woowagithubrepositoryapp.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ItemIssueBinding
import com.example.woowagithubrepositoryapp.model.Issue
import com.example.woowagithubrepositoryapp.utils.TimeUtil

class IssueAdapter : ListAdapter<Issue, IssueAdapter.IssueViewHolder>(IssueDiffCallback()) {
    class IssueViewHolder(
        private val binding: ItemIssueBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Issue) {
            binding.issue = item
            val resource = when(item.state){
                "open" -> R.drawable.ic_issue_open
                 else-> R.drawable.ic_issue_closed
            }
            binding.stateIconImageView.setImageResource(resource)
            binding.dateTextView.text = TimeUtil.getTimeData(item.updatedAt)
        }
    }

    class IssueDiffCallback : DiffUtil.ItemCallback<Issue>() {
        override fun areItemsTheSame(
            oldItem: Issue,
            newItem: Issue
        ): Boolean = (oldItem.id == newItem.id)

        override fun areContentsTheSame(
            oldItem: Issue,
            newItem: Issue
        ): Boolean = (oldItem.id == newItem.id) && (oldItem.title == newItem.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IssueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_issue,parent,false)
        val binding = ItemIssueBinding.bind(view)
        return IssueViewHolder(binding)
    }

    override fun onBindViewHolder(holder: IssueViewHolder, position: Int) {
        holder.bind(currentList[position])
    }


}