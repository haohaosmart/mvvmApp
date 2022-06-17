package com.wanghao.mvvmapp.ui.loginMvi

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wanghao.mvvmapp.mvvm.encrypt.EncryptionUtil
import com.wanghao.mvvmapp.mvvm.ktx.initiateRequest
import com.wanghao.mvvmapp.mvvm.mviCore.SharedFlowEvents
import com.wanghao.mvvmapp.mvvm.mviCore.setEvent
import com.wanghao.mvvmapp.mvvm.mviCore.setState
import com.wanghao.mvvmapp.mvvm.mviCore.withState
import com.wanghao.mvvmapp.mvvm.net.BaseResponse
import com.wanghao.mvvmapp.mvvm.viewModel.BaseViewModel
import com.wanghao.mvvmapp.ui.login.LoginRepo
import com.wanghao.mvvmapp.ui.login.bean.VerifyPasswordData
import com.wanghao.mvvmapp.ui.login.bean.VerifyPasswordRequest

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class FlowLoginViewModel: BaseViewModel() {
    private val loginRepo by lazy { LoginRepo(loadState) }

//    var mLoginData: MutableLiveData<BaseResponse<VerifyPasswordData>> = MutableLiveData()


    val TAG = "FlowLoginModel"
    private val _viewStates = MutableStateFlow(LoginViewState())
    val viewStates = _viewStates.asStateFlow()
    private val _viewEvents = SharedFlowEvents<LoginViewEvent>()
    val viewEvents = _viewEvents.asSharedFlow()


    fun dispatch(viewAction: LoginViewAction) {
        Log.d(TAG,"---dispatch action---")
        when (viewAction) {
            is LoginViewAction.UpdateUserName -> updateUserName(viewAction.userName)
            is LoginViewAction.UpdatePassword -> updatePassword(viewAction.password)
            is LoginViewAction.Login -> login()
        }
    }

    private fun updateUserName(userName: String) {
        Log.d(TAG,"----updateUserName---$userName")
        _viewStates.setState { copy(userName = userName) }

        Log.d(TAG, "----_viewStates value : ${_viewStates.value}")

        Log.d(TAG,"---viewStates value : ${viewStates.value}")
    }

    private fun updatePassword(password: String) {
        Log.d(TAG,"----updatePassword---$password")
        _viewStates.setState { copy(password = password) }
        Log.d(TAG, "----_viewStates value : ${_viewStates.value}")
    }

    private fun login() {
        // 发起一个携程
        viewModelScope.launch {
            flow {
                loginLogic()
                emit("登录请求成功成功")
            }.onStart {
                _viewEvents.setEvent(LoginViewEvent.ShowLoadingDialog)
            }.onEach {
                _viewEvents.setEvent(
                    LoginViewEvent.DismissLoadingDialog
                )
            }.catch {
                _viewStates.setState { copy(password = "") }
                _viewEvents.setEvent(
                    LoginViewEvent.DismissLoadingDialog
                )
            }.collect()
        }
    }

    private suspend fun loginLogic() {
        withState(viewStates) {
            val userName = it.userName
            val password = it.password


            val verifyPasswordRequest = VerifyPasswordRequest(
                longitude = "",
                latitude = "",
                deviceId = "123123",
                areaCode = "+86",
                //  areaCode = areaCode,
                phoneNo = userName,
                passWord = EncryptionUtil.md5(password)
            )
            val response = loginRepo.reqLogin(verifyPasswordRequest)
            _viewEvents.setEvent(
                LoginViewEvent.LoginRequestFinish(response)
            )
        }
    }
}

