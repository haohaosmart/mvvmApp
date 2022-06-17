package com.wanghao.mvvmapp.ui.login

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import com.wanghao.mvvmapp.databinding.ActivityLoginBinding
import com.wanghao.mvvmapp.mvvm.view.BaseActivity
import com.wanghao.mvvmapp.ui.loginMvi.FlowLoginActivity

class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>() {
    companion object{
        const val TAG = "Login"
    }

    var testValue: Int = 0;


    override val mViewModel by viewModels<LoginViewModel>()
    override fun ActivityLoginBinding.initView() {
      mBinding.btLogin.setOnClickListener {
          val areaCode = "+86"
          val account = mBinding.etAccount.text.toString()
          val pwd = mBinding.edPwd.text.toString()
          mViewModel.reqLogin(areaCode, account, pwd)
      }
        mBinding.ivLogo.setOnClickListener {
            val intent = Intent(this@LoginActivity, FlowLoginActivity::class.java)
            startActivity(intent)
        }
    }

    override fun initObserve() {
        mViewModel.mLoginData.observe(this) {
            testValue ++
            Log.d(TAG,"----onObserve----$testValue")
            if (it.code == "200") {
                Toast.makeText(this@LoginActivity, it.msg, Toast.LENGTH_LONG).show()
            }

        }


        mViewModel.mTestLiveValue.observe(this) {
            Log.d(TAG, "----onObserve of testLiveValue:$it")
        }

        mViewModel.loadState.observe(this) {

        }
    }

    override fun initRequestData() {

    }
}