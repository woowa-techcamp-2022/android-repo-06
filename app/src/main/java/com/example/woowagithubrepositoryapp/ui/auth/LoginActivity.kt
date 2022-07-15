package com.example.woowagithubrepositoryapp.ui.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.woowagithubrepositoryapp.R
import com.example.woowagithubrepositoryapp.databinding.ActivityLoginBinding
import com.example.woowagithubrepositoryapp.ui.MainActivity
import com.example.woowagithubrepositoryapp.utils.Constants
import com.example.woowagithubrepositoryapp.utils.Constants.GITHUB_CLIENT_ID
import com.example.woowagithubrepositoryapp.utils.Prefs
import com.example.woowagithubrepositoryapp.utils.clearTasksAndStartActivity

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityLoginBinding>(
            this,
            R.layout.activity_login
        )
    }
    private val viewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        checkToken()
        checkIntent()

        binding.loginLoginButton.setOnClickListener {
            login()
        }
    }

    private fun checkToken(){
        if (Prefs.accessToken.isNotEmpty()){
            clearTasksAndStartActivity<MainActivity>()
        }
    }

    private fun checkIntent(){
        intent?.data?.getQueryParameter("code")?.let {
            viewModel.code.value = it
            viewModel.getToken()
        }
    }

    private fun login() {
        val loginUrl = "https://github.com/login/oauth/authorize?client_id=${GITHUB_CLIENT_ID}&scope=user%20repo"
        val intent = Intent(Intent.ACTION_VIEW,loginUrl.toUri())
        startActivity(intent)
    }
}