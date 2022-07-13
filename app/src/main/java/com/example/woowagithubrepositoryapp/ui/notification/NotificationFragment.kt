package com.example.woowagithubrepositoryapp.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.woowagithubrepositoryapp.databinding.FragmentNotificationBinding
import com.example.woowagithubrepositoryapp.ui.adapter.NotificationAdapter

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy  {
        ViewModelProvider(this)[NotificationViewModel::class.java]
    }

    private val notificationAdapter by lazy {
        NotificationAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container,false)
        binding.recyclerviewNotification.adapter = notificationAdapter

        viewModel.notifications.observe(viewLifecycleOwner){
            notificationAdapter.submitList(it)
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}