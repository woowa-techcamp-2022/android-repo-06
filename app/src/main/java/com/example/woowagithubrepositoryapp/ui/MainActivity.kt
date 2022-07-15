package com.example.woowagithubrepositoryapp.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ActivityMainBinding
import com.example.woowagithubrepositoryapp.ui.issue.IssueFragment
import com.example.woowagithubrepositoryapp.ui.notification.NotificationFragment
import com.example.woowagithubrepositoryapp.ui.profile.ProfileActivity
import com.example.woowagithubrepositoryapp.ui.search.SearchActivity
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity(), TabLayout.OnTabSelectedListener {

    private var issueFragment : IssueFragment? = null
    private var notificationFragment : NotificationFragment? = null

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

        viewModel.getUserData(){invalidateOptionsMenu()}

    }

    private fun initTabLayout(tabLayout: TabLayout) {
        resources.getStringArray(R.array.main_tab).forEach { tabName ->
            tabLayout.addTab(tabLayout.newTab().setText(tabName))
        }

        tabLayout.addOnTabSelectedListener(this)
        tabLayout.selectTab(tabLayout.getTabAt(0))
    }

    private fun initToolbar(toolbar: MaterialToolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = getString(R.string.appbar_title)
        toolbar.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.nav_profile -> startProfileActivity()
                R.id.nav_search -> startSearchActivity()
            }
            return@setOnMenuItemClickListener true
        }
    }

    private fun startProfileActivity() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun startSearchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        tab?.let {
            when (it.text) {
                "Issue" -> {
                    if(issueFragment == null)
                        issueFragment = IssueFragment()
                    changeFragmentToIssueFragment()
                }
                else -> {
                    if(notificationFragment == null)
                        notificationFragment = NotificationFragment()
                    changeFragmentToNotificationFragment()
                }
            }
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        tab?.let {
            when (it.text) {
                "Issue" -> {
                    issueFragment = IssueFragment()
                    changeFragmentToIssueFragment()
                }
                else -> {
                    notificationFragment = NotificationFragment()
                    changeFragmentToNotificationFragment()
                }
            }
        }
    }
    private fun changeFragmentToIssueFragment(){
        issueFragment.let {
            if (it != null) {
                supportFragmentManager.beginTransaction().replace(
                    binding.containerMain.id, it
                ).commit()
            }
        }
    }
    private fun changeFragmentToNotificationFragment(){
        notificationFragment.let {
            if (it != null) {
                supportFragmentManager.beginTransaction().replace(
                    binding.containerMain.id, it
                ).commit()
            }
        }
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val url = App.user?.avatarUrl
        println("${App.user?.name}")
        Glide.with(this).asDrawable().load(url).transform(CircleCrop())
            .into(object : CustomTarget<Drawable>(){
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    menu?.findItem(R.id.nav_profile)?.let {
                        it.icon = resource
                    }
                }
                override fun onLoadCleared(placeholder: Drawable?) {
                }
        })
        return super.onPrepareOptionsMenu(menu)
    }
}
