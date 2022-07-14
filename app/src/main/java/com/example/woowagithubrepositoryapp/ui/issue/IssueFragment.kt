package com.example.woowagithubrepositoryapp.ui.issue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.FragmentIssueBinding
import com.example.woowagithubrepositoryapp.ui.adapter.IssueAdapter
import com.example.woowagithubrepositoryapp.ui.adapter.SpinnerAdapter

class IssueFragment : Fragment() {

    private lateinit var binding : FragmentIssueBinding

    private val viewModel by lazy {
        ViewModelProvider(this)[IssueViewModel::class.java]
    }

    private val issueAdapter = IssueAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_issue,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner

        initSpinner()
        initRecyclerView()
    }

    private fun initSpinner(){
        val list = listOf(
            SpinnerAdapter.IssueOption("Open", true),
            SpinnerAdapter.IssueOption("Close", false),
            SpinnerAdapter.IssueOption("All", false),
        )
        val adapter = SpinnerAdapter(requireContext(), R.layout.item_spinner, list)
        binding.issueSpinner.adapter = adapter
        binding.issueSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when(p2){
                    0 -> {
                        viewModel.selectState = "open"
                    }
                    1 -> {
                        viewModel.selectState = "closed"
                    }
                    2 -> {
                        viewModel.selectState = "all"
                    }
                }
                viewModel.pageNumber = 1
                viewModel.issueList.clear()
                loadIssueData()
                for (idx in list.indices) {
                    list[idx].check = (idx == p2)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    private fun loadIssueData(){
        viewModel.getIssues("all",viewModel.selectState,viewModel.pageNumber){
            issueAdapter.submitList(it.toMutableList())
        }
    }

    private fun initRecyclerView(){
        binding.issueRecyclerView.adapter = issueAdapter
        binding.issueRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadIssueData()

        binding.issueRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount
                if (lastVisibleItemPosition + 1 == itemTotalCount) {
                    viewModel.pageNumber++
                    loadIssueData()
                }
            }
        })
    }
}