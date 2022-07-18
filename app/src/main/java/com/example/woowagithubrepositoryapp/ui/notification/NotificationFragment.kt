package com.example.woowagithubrepositoryapp.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.woowagithubrepositoryapp.utils.Constants.DataLoading
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.databinding.FragmentNotificationBinding
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.ui.MainViewModel
import com.example.woowagithubrepositoryapp.ui.adapter.NotificationAdapter
import com.example.woowagithubrepositoryapp.utils.Constants
import com.example.woowagithubrepositoryapp.utils.NotificationItemHelper
import com.example.woowagithubrepositoryapp.utils.ViewModelFactory

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), ViewModelFactory())[MainViewModel::class.java]
    }

    private val notificationAdapter = NotificationAdapter()

    private fun initRecyclerView(){
        ItemTouchHelper(NotificationItemHelper(requireContext()) { notification ->
            markNotification(notification)
        }).attachToRecyclerView(binding?.recyclerviewNotification)

        binding?.recyclerviewNotification?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount
                if (lastVisibleItemPosition + 1 == itemTotalCount) {
                    if(viewModel.isNotificationDataLoading != DataLoading.NOW) {
                        viewModel.isNotificationDataLoading = DataLoading.NOW
                        viewModel.getNotifications()
                        Log.d("notificationPaging", "notificationPaging")
                    }
                }
            }
        })
    }

    private fun markNotification(notification:Notification){
        viewModel.markNotificationAsRead(notification = notification)
        Toast.makeText(
            context, "${notification.subject.title} 알림이 읽음 처리되었습니다", Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNotificationBinding.inflate(inflater, container, false)



        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.recyclerviewNotification?.adapter = notificationAdapter

        viewModel.notifications.observe(viewLifecycleOwner){
            notificationAdapter.submitList(it.toMutableList())
        }

        initRecyclerView()

        viewModel.getNotifications()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}