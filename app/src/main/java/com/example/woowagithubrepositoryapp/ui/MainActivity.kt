package com.example.woowagithubrepositoryapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ActivityMainBinding
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

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
        resources.getStringArray(R.array.main_tab).forEach { tabName ->
            tabLayout.addTab(tabLayout.newTab().setText(tabName))
        }

        tabLayout.addOnTabSelectedListener(this)
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

    override fun onTabSelected(tab: TabLayout.Tab?) {
        Toast.makeText(this, "${tab?.text} 클릭",Toast.LENGTH_SHORT).show()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

}
