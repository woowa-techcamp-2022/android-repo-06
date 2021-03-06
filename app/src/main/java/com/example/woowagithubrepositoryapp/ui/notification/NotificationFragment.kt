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
import kotlinx.coroutines.*

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

        initRecyclerView()
        initNotifications()
        setObserver()
    }

    override fun onStop() {
        viewModel.refreshNotifications()
        super.onStop()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initRecyclerView() {
        binding?.let {
            it.notificationRecyclerView.apply {
                ItemTouchHelper(NotificationItemHelper(requireContext()) { notification ->
                    markNotification(notification)
                }).attachToRecyclerView(this)

                this.addOnScrollListener(object : RecyclerView.OnScrollListener(){
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

            it.notificationSwipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshNotifications()
                it.notificationSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun initNotifications() {
        if(viewModel.notifications.value?.size == 0)
            viewModel.getNotifications()
    }

    private fun markNotification(notification: Notification) {
        viewModel.removeNotification(notification = notification)
    }

    private fun setObserver(){
        viewModel.notifications.observe(viewLifecycleOwner) {
            notificationAdapter.submitList(it.toMutableList())
        }
    }
}