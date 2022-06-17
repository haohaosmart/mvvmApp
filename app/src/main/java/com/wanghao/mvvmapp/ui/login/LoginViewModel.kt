package com.wanghao.mvvmapp.ui.login

import androidx.lifecycle.MutableLiveData
import com.wanghao.mvvmapp.mvvm.encrypt.EncryptionUtil
import com.wanghao.mvvmapp.mvvm.ktx.initiateRequest
import com.wanghao.mvvmapp.mvvm.net.BaseResponse
import com.wanghao.mvvmapp.mvvm.viewModel.BaseViewModel
import com.wanghao.mvvmapp.ui.login.bean.VerifyPasswordData
import com.wanghao.mvvmapp.ui.login.bean.VerifyPasswordRequest

class LoginViewModel : BaseViewModel() {
    private val loginRepo by lazy { LoginRepo(loadState)}


    var mLoginData: MutableLiveData<BaseResponse<VerifyPasswordData>> = MutableLiveData()

    var mTestLiveValue : MutableLiveData<Int> = MutableLiveData(0)

    fun reqLogin(areaCode: String, phoneNo: String, pwd: String) {


        val verifyPasswordRequest = VerifyPasswordRequest(
            longitude = "",
            latitude = "",
            deviceId = "123123",
            areaCode = areaCode,
            //  areaCode = areaCode,
            phoneNo = phoneNo,
            passWord = EncryptionUtil.md5(pwd)
        )
        initiateRequest({
            mLoginData.value = loginRepo.reqLogin(verifyPasswordRequest)
            mTestLiveValue.value = 22
        }, loadState)
    }


}