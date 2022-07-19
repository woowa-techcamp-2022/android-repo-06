package com.example.woowagithubrepositoryapp.ui.issue

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.FragmentIssueBinding
import com.example.woowagithubrepositoryapp.ui.MainViewModel
import com.example.woowagithubrepositoryapp.ui.adapter.IssueAdapter
import com.example.woowagithubrepositoryapp.ui.adapter.SpinnerAdapter
import com.example.woowagithubrepositoryapp.utils.ViewModelFactory

class IssueFragment : Fragment() {

    private lateinit var binding: FragmentIssueBinding

    private val viewModel by lazy {
        ViewModelProvider(requireActivity(), ViewModelFactory())[MainViewModel::class.java]
    }

    private val issueAdapter = IssueAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_issue, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        initSpinner()
        initRecyclerView()
    }

    private fun initSpinner() {
        val list = listOf(
            SpinnerAdapter.IssueOption("Open", true),
            SpinnerAdapter.IssueOption("Close", false),
            SpinnerAdapter.IssueOption("All", false),
        )
        val adapter = SpinnerAdapter(requireContext(), R.layout.item_spinner, list)
        binding.issueSpinner.adapter = adapter
        binding.issueSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (position) {
                    0 -> {
                        viewModel.issueSelectState.value = "open"
                    }
                    1 -> {
                        viewModel.issueSelectState.value = "closed"
                    }
                    2 -> {
                        viewModel.issueSelectState.value = "all"
                    }
                }
                viewModel.issuePage.value = 1
                viewModel.issueList.clear()
                loadIssueData()
                for (idx in list.indices) {
                    list[idx].check = (idx == position)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }

        when(viewModel.issueSelectState.value){
            "open" -> binding.issueSpinner.setSelection(0)
            "closed" -> binding.issueSpinner.setSelection(1)
            "all" -> binding.issueSpinner.setSelection(2)
        }
    }

    private fun loadIssueData() {
        viewModel.getIssues {
            issueAdapter.submitList(it.toMutableList())
        }
    }

    private fun initRecyclerView() {
        binding.issueRecyclerView.adapter = issueAdapter
        binding.issueRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.issueRecyclerView.itemAnimator = null

        loadIssueData()

        binding.issueRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount
                if (lastVisibleItemPosition + 1 == itemTotalCount) {
                    viewModel.issuePage.value = viewModel.issuePage.value!! + 1
                    loadIssueData()
                }
            }
        })
    }
}