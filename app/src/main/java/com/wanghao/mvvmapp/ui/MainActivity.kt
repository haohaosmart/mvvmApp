package com.wanghao.mvvmapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wanghao.mvvmapp.R
import com.wanghao.mvvmapp.databinding.ActivityMainBinding
import com.wanghao.mvvmapp.ui.dialog.DialogTestActivity
import com.wanghao.mvvmapp.ui.login.LoginActivity
import com.wanghao.mvvmapp.ui.loginCompose.ComposeLoginActivity
import com.wanghao.mvvmapp.ui.loginMvi.FlowLoginActivity
import com.wanghao.mvvmapp.ui.navTab.NavTabActivity
import com.wanghao.mvvmapp.ui.paging.PagingForUserActivity

class MainActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.tv0.setOnClickListener(this)
        binding.tv1.setOnClickListener(this)
        binding.tv2.setOnClickListener(this)
        binding.tv3.setOnClickListener(this)
        binding.tv4.setOnClickListener(this)
        binding.tv5.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.tv0 -> {
                Intent().apply {
                    setClass(this@MainActivity, LoginActivity::class.java)
                    startActivity(this)
                }
            }
            R.id.tv1 -> {
                Intent().apply {
                    setClass(this@MainActivity, FlowLoginActivity::class.java)
                    startActivity(this)
                }
            }
            R.id.tv2 -> {
                Intent().apply {
                    setClass(this@MainActivity, ComposeLoginActivity::class.java)
                    startActivity(this)
                }
            }
            R.id.tv3 -> {
                Intent().apply {
                    setClass(this@MainActivity, PagingForUserActivity::class.java)
                    startActivity(this)
                }
            }
            R.id.tv4 -> {
                Intent().apply {
                    setClass(this@MainActivity, NavTabActivity::class.java)
                    startActivity(this)
                }
            }
            R.id.tv5 -> {
                Intent().apply {
                    setClass(this@MainActivity, DialogTestActivity::class.java)
                    startActivity(this)
                }
            }
        }
    }
}