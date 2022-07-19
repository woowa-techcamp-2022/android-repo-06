package com.example.woowagithubrepositoryapp.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ActivitySearchBinding
import com.example.woowagithubrepositoryapp.ui.adapter.RepositoryAdapter
import com.example.woowagithubrepositoryapp.utils.ViewModelFactory
import kotlinx.coroutines.Job

class SearchActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivitySearchBinding>(
            this,
            R.layout.activity_search
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory())[SearchViewModel::class.java]
    }

    private val repoAdapter = RepositoryAdapter(this)

    private var checkTextJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initToolbar()
        initRecyclerView()
        initEditText()
        setObserver()
    }

    private fun initToolbar() {
        setSupportActionBar(binding.searchToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }

    private fun initRecyclerView() {
        binding.searchRepoRecyclerView.adapter = repoAdapter
        binding.searchRepoRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.searchRepoRecyclerView.itemAnimator = null
        binding.searchRepoRecyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val lastVisibleItemPosition =
                    (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                val itemTotalCount = recyclerView.adapter?.itemCount
                if (lastVisibleItemPosition + 1 == itemTotalCount) {
                    viewModel.pageNumber++
                    viewModel.searchRepos {
                        repoAdapter.submitList(it)
                        repoAdapter.notifyItemRangeChanged(itemTotalCount, it.size)
                    }
                }
            }
        })
    }

    private fun initEditText() {
        binding.searchBarEditText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchRepos()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }

    private fun searchRepos() {
        viewModel.repoList.clear()
        viewModel.pageNumber = 1
        viewModel.searchRepos {
            repoAdapter.submitList(it)
            repoAdapter.notifyDataSetChanged()
        }
    }

    private fun setObserver() {
        viewModel.searchText.observe(this) {
            if (it.isEmpty()) {
                setSearchBarInactive()
            } else {
                if (viewModel.isSearchBarActive.value == false)
                    setSearchBarActive()
                checkTextJob?.cancel()
                checkTextJob = viewModel.checkText(it) { searchRepos() }
            }
        }
    }

    private fun setSearchBarActive() {
        viewModel.isSearchBarActive.value = true
    }

    private fun setSearchBarInactive() {
        viewModel.isSearchBarActive.value = false
        viewModel.isRecyclerViewOn.value = false
        viewModel.repoList.clear()
        binding.searchRepoRecyclerView.invalidate()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}