package com.example.woowagithubrepositoryapp.ui.profile

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.example.woowagithubrepositoryapp.App
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ActivityProfileBinding
import com.example.woowagithubrepositoryapp.utils.ViewModelFactory

class ProfileActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityProfileBinding>(
            this,
            R.layout.activity_profile
        )
    }

    private val viewModel by lazy {
        ViewModelProvider(this, ViewModelFactory())[ProfileViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        initAppbar()
        if (App.user != null){
            initView()
        }
    }

    private fun initAppbar() {
        setSupportActionBar(binding.profileToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_arrow_left)
    }

    private fun initView(){
        App.user?.let {
            Glide.with(this)
                .load(it.avatarUrl)
                .transform(CircleCrop())
                .into(binding.profileImageView)
        }
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