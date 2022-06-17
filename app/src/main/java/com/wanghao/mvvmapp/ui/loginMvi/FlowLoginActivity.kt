package com.wanghao.mvvmapp.ui.loginMvi

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.wanghao.mvvmapp.databinding.ActivityLoginBinding
import com.wanghao.mvvmapp.mvvm.mviCore.observeEvent
import com.wanghao.mvvmapp.mvvm.mviCore.observeState
import com.wanghao.mvvmapp.mvvm.view.BaseActivity
import com.wanghao.mvvmapp.ui.login.LoginViewModel


class FlowLoginActivity : BaseActivity<ActivityLoginBinding, FlowLoginViewModel>() {

    val TAG = "FlowLogin"

    override val mViewModel by viewModels<FlowLoginViewModel>()

    override fun ActivityLoginBinding.initView() {
        mBinding.etAccount.addTextChangedListener {
            mViewModel.dispatch(LoginViewAction.UpdateUserName(it.toString()))
        }

        mBinding.edPwd.addTextChangedListener {
            mViewModel.dispatch(LoginViewAction.UpdatePassword(it.toString()))
        }
        mBinding.btLogin.setOnClickListener {
            mViewModel.dispatch(LoginViewAction.Login)
        }
    }

    override fun initObserve() {


//        mViewModel.viewStates.observeState(this, LoginViewState::userName){
//            Log.d(TAG,"---states----userName---$it")
//        }
        // 表单状态的监听
        mViewModel.viewStates.let { states ->
            Log.d(TAG,"---states----" + states.value)
            states.observeState(this, LoginViewState::userName) {
                Log.d(TAG,"----account observeState----")
                mBinding.etAccount.setText(it)
                mBinding.etAccount.setSelection(it.length)
            }
            states.observeState(this, LoginViewState::password) {
                Log.d(TAG,"----password observeState----")
                mBinding.edPwd.setText(it)
                mBinding.edPwd.setSelection(it.length)
            }
            states.observeState(this, LoginViewState::isLoginEnable) {
                Log.d(TAG,"----isLoginEnable----$it" )

                mBinding.btLogin.isEnabled = it
                mBinding.btLogin.alpha = if (it) 1f else 0.5f
            }

        }


        // 调用登录接口 数据的监听
        mViewModel.viewEvents.observeEvent(this) {
            when (it) {
               is LoginViewEvent.LoginRequestFinish -> {
                   Log.d(TAG,"----" + it.response.msg)
               }
                is LoginViewEvent.ShowLoadingDialog -> showLoadingDialog()
                is LoginViewEvent.DismissLoadingDialog -> dismissLoadingDialog()
            }
        }
    }

    override fun initRequestData() {
    }


    private var progressDialog: ProgressDialog? = null

    private fun showLoadingDialog() {
        if (progressDialog == null)
            progressDialog = ProgressDialog(this)
        progressDialog?.show()
    }

    private fun dismissLoadingDialog() {
        progressDialog?.takeIf { it.isShowing }?.dismiss()
    }

}

