package com.example.woowagithubrepositoryapp.ui.notification

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.databinding.FragmentNotificationBinding
import com.example.woowagithubrepositoryapp.model.Notification
import com.example.woowagithubrepositoryapp.ui.MainViewModel
import com.example.woowagithubrepositoryapp.ui.adapter.NotificationAdapter
import com.example.woowagithubrepositoryapp.ui.common.DataLoading
import com.example.woowagithubrepositoryapp.ui.common.ViewModelFactory

class NotificationFragment : Fragment() {

    private var _binding: FragmentNotificationBinding? = null
    private val binding get() = _binding

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), ViewModelFactory())[MainViewModel::class.java]
    }

    private val notificationAdapter = NotificationAdapter()

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
        binding?.notificationRecyclerView?.adapter = notificationAdapter

        viewModel.notifications.observe(viewLifecycleOwner) {
            notificationAdapter.submitList(it.toMutableList())
        }

        initRecyclerView()

        getNotifications()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initRecyclerView() {
        ItemTouchHelper(NotificationItemHelper(requireContext()) { notification,position ->
            markNotification(notification,position)
        }).attachToRecyclerView(binding?.notificationRecyclerView)

        binding?.notificationRecyclerView?.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount
                if (lastVisibleItemPosition + 1 == itemTotalCount) {
                    if (viewModel.isNotificationDataLoading == DataLoading.BEFORE) {
                        viewModel.isNotificationDataLoading = DataLoading.NOW
                        viewModel.getNotifications()
                        Log.d("notificationPaging", "notificationPaging")
                    }
                }
            }
        })
    }

    private fun redrawNotificationAdapterItemAtPosition(position : Int){
        notificationAdapter.notifyItemChanged(position)
    }

    private fun getNotifications() {
        if(viewModel.notifications.value?.size == 0)
            viewModel.getNotifications()
    }

    private fun markNotification(notification: Notification,position: Int) {
        viewModel.markNotificationAsRead(notification = notification) {
            redrawNotificationAdapterItemAtPosition(position)
        }
    }
}