package com.example.woowagithubrepositoryapp.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.woowagithubrepositoryapp.databinding.FragmentNotificationBinding
import com.example.woowagithubrepositoryapp.ui.adapter.NotificationAdapter
import com.example.woowagithubrepositoryapp.utils.NotificationItemHelper

class NotificationFragment : Fragment() {
    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding!!
    private val viewModel by lazy  {
        ViewModelProvider(this)[NotificationViewModel::class.java]
    }

    private val notificationAdapter by lazy {
        NotificationAdapter()
    }

    private fun initRecyclerView(){
        ItemTouchHelper(
            NotificationItemHelper(
                requireContext(),
                {
                    markNotification(it)
                }))
            .attachToRecyclerView(binding.recyclerviewNotification)
    }
    fun markNotification(threadId:String){
        viewModel.markNotificationAsRead(threadId= threadId)
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
        initRecyclerView()
        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}