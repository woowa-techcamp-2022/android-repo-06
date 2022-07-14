package com.example.woowagithubrepositoryapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.databinding.ItemNotificationBinding
import com.example.woowagithubrepositoryapp.model.Notification
import android.os.Handler
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.htmlEncode
import androidx.lifecycle.lifecycleScope
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.model.NotificationSubjectURLResponse
import com.example.woowagithubrepositoryapp.network.GithubService
import com.example.woowagithubrepositoryapp.ui.MainActivity
import kotlinx.coroutines.launch
import okio.ByteString.Companion.encode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Url
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL

class NotificationAdapter : ListAdapter<Notification,NotificationAdapter.NotificationViewHolder>(MenuDiffCallback()) {

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
        Thread {
            runCatching {
                try{
                    val url = getItem(position).subject.url
                    val response = GithubService.instance.getNotificationInfo(url)
                    response.enqueue(object : Callback<NotificationSubjectURLResponse> {
                        override fun onResponse(
                            call: Call<NotificationSubjectURLResponse>,
                            response: Response<NotificationSubjectURLResponse>
                        ) {
                            holder.itemView.findViewById<AppCompatTextView>(R.id.comment_textView).text =
                                 response.body()?.comments.toString()
                            holder.itemView.findViewById<AppCompatTextView>(R.id.issue_textView).text =
                                "#" + response.body()?.number.toString()
                        }
                        override fun onFailure(
                            call: Call<NotificationSubjectURLResponse>,
                            t: Throwable
                        ) {
                            Log.d("notificationInfo",t.cause.toString())
                        }

                    })
                } catch (e : Exception){
                    Log.d("notificationInfo",e.cause.toString())
                }

            }
        }.start()
    }

    inner class NotificationViewHolder(val binding : ItemNotificationBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(item : Notification){
            binding.apply {
                this.notification = item
                executePendingBindings()

            }
        }
    }

    private class MenuDiffCallback : DiffUtil.ItemCallback<Notification>(){
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification): Boolean {
            return oldItem.subject.title == newItem.subject.title
        }

    }


}