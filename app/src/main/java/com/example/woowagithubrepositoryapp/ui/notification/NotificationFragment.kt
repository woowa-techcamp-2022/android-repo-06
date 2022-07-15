package com.example.woowagithubrepositoryapp.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.woowagithubrepositoryapp.databinding.FragmentNotificationBinding
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.ui.adapter.NotificationAdapter
import com.example.woowagithubrepositoryapp.utils.NotificationItemHelper
import com.example.woowagithubrepositoryapp.utils.ViewModelFactory
import kotlinx.coroutines.launch

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory())[NotificationViewModel::class.java]
    }

    private val notificationAdapter by lazy {
        NotificationAdapter()
    }

    private fun initRecyclerView() {
        ItemTouchHelper(NotificationItemHelper(
            requireContext()
        ) { notification, position ->
            markNotification(notification, position)
        }).attachToRecyclerView(binding.recyclerviewNotification)
    }

    private fun markNotification(notification: Notification?, position: Int) {
        lifecycleScope.launch {
            val isRead =
                viewModel.markNotificationAsRead(threadId = notification?.threadId.toString())
            if (isRead) {
                removeNotification(position)
                Toast.makeText(
                    context,
                    "${notification?.subject?.title.toString()} 알림이 읽음 처리되었습니다",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun removeNotification(position: Int) {
        viewModel.removeNotificationAtPosition(position)
        notificationAdapter.notifyItemRemoved(position)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)
        binding.recyclerviewNotification.adapter = notificationAdapter

        viewModel.notifications.observe(viewLifecycleOwner) {
            notificationAdapter.submitList(it)
        }
        initRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}