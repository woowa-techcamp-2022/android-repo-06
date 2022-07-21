package com.example.woowagithubrepositoryapp.ui.issue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.FragmentIssueBinding
import com.example.woowagithubrepositoryapp.ui.MainViewModel
import com.example.woowagithubrepositoryapp.ui.adapter.IssueAdapter
import com.example.woowagithubrepositoryapp.ui.adapter.SpinnerAdapter
import com.example.woowagithubrepositoryapp.utils.Constants
import com.example.woowagithubrepositoryapp.utils.ViewModelFactory

class IssueFragment : Fragment() {

    private var _binding: FragmentIssueBinding? = null
    private val binding get() = _binding

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), ViewModelFactory())[MainViewModel::class.java]
    }

    private val issueAdapter = IssueAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIssueBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.issueLoadType = Constants.IssueLoadType.CREATE

        initSpinner()
        initRecyclerView()
        setObserver()
    }

    private fun initSpinner() {
        val list = listOf(
            SpinnerAdapter.IssueOption("Open", true),
            SpinnerAdapter.IssueOption("Close", false),
            SpinnerAdapter.IssueOption("All", false),
        )
        val adapter = SpinnerAdapter(requireContext(), R.layout.item_spinner, list)
        binding?.let {
            it.issueSpinner.adapter = adapter
            it.issueSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    val newValue = when (position) {
                        0 -> "open"
                        1 -> "closed"
                        else -> "all"
                    }
                    if (viewModel.issueSelectState.value != newValue) {
                        viewModel.issuePage = 1
                        viewModel.issueSelectState.value = newValue
                        viewModel.issueList.clear()
                        viewModel.issueLoadType = Constants.IssueLoadType.LOAD
                    }
                    loadIssueData()
                    for (idx in list.indices) {
                        list[idx].check = (idx == position)
                    }
                    adapter.notifyDataSetChanged()
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {

                }
            }

            when (viewModel.issueSelectState.value) {
                "open" -> it.issueSpinner.setSelection(0)
                "closed" -> it.issueSpinner.setSelection(1)
                "all" -> it.issueSpinner.setSelection(2)
            }
        }
    }

    private fun loadIssueData() {
        if (viewModel.issueLoadType == Constants.IssueLoadType.CREATE && viewModel.issueList.isNotEmpty()) {
            issueAdapter.submitList(viewModel.issueList.toMutableList())
        } else {
            viewModel.getIssues {
                issueAdapter.submitList(it.toMutableList())
            }
        }
    }

    private fun initRecyclerView() {
        binding?.let {
            it.issueRecyclerView.adapter = issueAdapter
            it.issueRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            it.issueRecyclerView.itemAnimator = null

            it.issueRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val itemTotalCount = recyclerView.adapter?.itemCount
                    if (lastVisibleItemPosition + 1 == itemTotalCount) {
                        viewModel.issuePage++
                        viewModel.issueLoadType = Constants.IssueLoadType.PAGING
                        loadIssueData()
                    }
                }
            })

            it.issueSwipeRefreshLayout.setOnRefreshListener {
                viewModel.refreshIssues()
                it.issueSwipeRefreshLayout.isRefreshing = false
            }
        }
    }

    private fun setObserver() {
        viewModel.issueRefreshState.observe(viewLifecycleOwner) {
            loadIssueData()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}