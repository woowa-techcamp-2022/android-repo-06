package com.example.woowagithubrepositoryapp.ui

import android.content.Intent
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ActivityMainBinding
import com.example.woowagithubrepositoryapp.ui.common.Tab
import com.example.woowagithubrepositoryapp.ui.common.TabSelectState
import com.example.woowagithubrepositoryapp.ui.common.ViewModelFactory
import com.example.woowagithubrepositoryapp.ui.issue.IssueFragment
import com.example.woowagithubrepositoryapp.ui.notification.NotificationFragment
import com.example.woowagithubrepositoryapp.ui.profile.ProfileActivity
import com.example.woowagithubrepositoryapp.ui.search.SearchActivity
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
        ViewModelProvider(this, ViewModelFactory())[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initTabLayout(binding.mainTabLayout)
        initToolbar(binding.mainToolbar)
        setStateObserve()
        if(App.user == null){
            viewModel.getUserData { invalidateOptionsMenu() }
        }
    }

    private fun setStateObserve() {
        viewModel.tabSelectState.observe(this) {
            when (setOf(it.text,it.isReselected)) {
                setOf(Tab.NOTI.text,true)  -> {
                    viewModel.refreshNotifications()
                }
                setOf(Tab.NOTI.text,false)  -> {
                    changeFragmentByTag(it.text)
                }
                setOf(Tab.ISSUE.text,true)  -> {
                    viewModel.refreshIssues()
                }
                setOf(Tab.ISSUE.text,false)  -> {
                    changeFragmentByTag(it.text)
                }
            }
        }
    }

    private fun initTabLayout(tabLayout: TabLayout) {
        resources.getStringArray(R.array.main_tab).forEach { tabName ->
            tabLayout.addTab(tabLayout.newTab().setText(tabName))
        }

        tabLayout.addOnTabSelectedListener(this)
        tabLayout.selectTab(tabLayout.getTabAt(-1))

        when (viewModel.tabSelectState.value?.text) {
            Tab.ISSUE.text -> tabLayout.selectTab(tabLayout.getTabAt(0))
            Tab.NOTI.text -> tabLayout.selectTab(tabLayout.getTabAt(1))
        }
    }

    private fun initToolbar(toolbar: MaterialToolbar) {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        toolbar.title = getString(R.string.main_title)
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
            viewModel.tabSelectState.value = TabSelectState(it.text.toString(),false)
        }
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
        tab?.let {
            viewModel.tabSelectState.value = TabSelectState(it.text.toString(),true)
        }
    }

    private fun changeFragmentByTag(tag: String){
        val transaction = supportFragmentManager.beginTransaction()
        val showFragment = supportFragmentManager.findFragmentByTag(tag)
        val hideFragment =
            if(Tab.ISSUE.text == tag) supportFragmentManager.findFragmentByTag(Tab.NOTI.text)
            else supportFragmentManager.findFragmentByTag(Tab.ISSUE.text)

        if(showFragment == null){
            transaction.add(binding.mainFrameLayout.id,getShowFragment(tag),tag)
        }
        transaction.apply {
            showFragment?.let {
                show(it)
            }
            hideFragment?.let {
                hide(it)
            }
        }.commit()
    }

    private fun getShowFragment(tag: String) : Fragment {
        return if(tag == Tab.ISSUE.text) IssueFragment() else NotificationFragment()
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        val url = App.user?.avatarUrl
        Glide.with(this).asDrawable().load(url).transform(CircleCrop())
            .into(object : CustomTarget<Drawable>() {
                override fun onResourceReady(
                    resource: Drawable,
                    transition: Transition<in Drawable>?
                ) {
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
