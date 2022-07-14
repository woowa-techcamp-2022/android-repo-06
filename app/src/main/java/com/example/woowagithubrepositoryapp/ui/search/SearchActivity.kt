package com.example.woowagithubrepositoryapp.ui.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ActivitySearchBinding
import com.example.woowagithubrepositoryapp.ui.adapter.RepositoryAdapter

class SearchActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivitySearchBinding>(
            this,
            R.layout.activity_search
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[SearchViewModel::class.java]
    }

    private val repoAdapter = RepositoryAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initRecyclerView()
        initEditText()
    }

    private fun initRecyclerView(){
        binding.searchRepoRecyclerView.adapter = repoAdapter
        binding.searchRepoRecyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun initEditText(){
        binding.searchBarEditText.setOnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH){
                viewModel.searchRepos { repoAdapter.submitList(it) }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
    }
}