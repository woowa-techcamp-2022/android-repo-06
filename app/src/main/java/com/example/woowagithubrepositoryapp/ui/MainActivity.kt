package com.example.woowagithubrepositoryapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this

        initTabLayout(binding.tablayoutMain)
        initToolbar(binding.toolbarMain)
    }

    private fun initTabLayout(tabLayout: TabLayout){
        val customTabLayout = R.layout.tab_main

        tabLayout.addTab( tabLayout.newTab().setCustomView(customTabLayout).apply {
            this.view.findViewById<AppCompatTextView>(R.id.textview_title_tab)?.text = getString(R.string.issue)
        })
        tabLayout.addTab(tabLayout.newTab().setCustomView(customTabLayout).apply {
           this.view.findViewById<AppCompatTextView>(R.id.textview_title_tab)?.text = getString(R.string.notifications)
        })

    }

    private fun initToolbar(toolbar: MaterialToolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = getString(R.string.appbar_title)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }
}
