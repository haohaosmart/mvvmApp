package com.wanghao.mvvmapp.ui.loginMvi

import com.wanghao.mvvmapp.mvvm.net.BaseResponse
import com.wanghao.mvvmapp.ui.login.bean.VerifyPasswordData


data class LoginViewState(val userName: String = "13752365561", val password: String = "Wh19910622") {
    val isLoginEnable: Boolean
        get() = userName.isNotEmpty() && password.length >= 6
    val passwordTipVisible: Boolean
        get() = password.length in 1..5
}

sealed class LoginViewEvent {
//    data class ShowToast(val message: String) : LoginViewEvent()
    data class LoginRequestFinish(val response: BaseResponse<VerifyPasswordData>): LoginViewEvent()
    object ShowLoadingDialog : LoginViewEvent()
    object DismissLoadingDialog : LoginViewEvent()
}

sealed class LoginViewAction {
    data class UpdateUserName(val userName: String) : LoginViewAction()
    data class UpdatePassword(val password: String) : LoginViewAction()
    object Login : LoginViewAction()
}