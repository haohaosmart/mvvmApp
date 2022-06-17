package com.wanghao.mvvmapp.ui.loginMvi

import androidx.lifecycle.MutableLiveData
import com.wanghao.mvvmapp.mvvm.ktx.dataConvert
import com.wanghao.mvvmapp.mvvm.model.BaseRepository
import com.wanghao.mvvmapp.mvvm.net.BaseResponse
import com.wanghao.mvvmapp.mvvm.net.RetrofitFactory
import com.wanghao.mvvmapp.mvvm.net.State
import com.wanghao.mvvmapp.ui.login.bean.VerifyPasswordData
import com.wanghao.mvvmapp.ui.login.bean.VerifyPasswordRequest
import retrofit2.http.Body
import retrofit2.http.POST

class LoginRepo(private val loadState: MutableLiveData<State>) : BaseRepository() {
    private val apiService by lazy {
        RetrofitFactory.instance.getService(LoginApiService::class.java)
    }

    suspend fun reqLogin(verifyPasswordRequest: VerifyPasswordRequest):  BaseResponse<VerifyPasswordData> {
        return apiService.reqVerifyPassword(verifyPasswordRequest);
    }

}


interface LoginApiService {
    @POST("/v210/sanlie-app-user-center" + "/login")
    suspend fun reqVerifyPassword(@Body verifyPasswordRequest: VerifyPasswordRequest): BaseResponse<VerifyPasswordData>
}