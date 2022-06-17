package com.wanghao.mvvmapp.ui.paging

import com.wanghao.mvvmapp.mvvm.net.BaseResponse
import com.wanghao.mvvmapp.mvvm.net.GithubRetrofitFactory
import com.wanghao.mvvmapp.mvvm.net.RetrofitFactory
import com.wanghao.mvvmapp.ui.login.LoginApiService
import com.wanghao.mvvmapp.ui.login.bean.VerifyPasswordData
import com.wanghao.mvvmapp.ui.login.bean.VerifyPasswordRequest
import com.wanghao.mvvmapp.ui.paging.bean.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

class PagingRepo {

    private val apiService by lazy {
        GithubRetrofitFactory.instance.getService(PagingApiService::class.java)
    }

    suspend fun reqUsers(since: Int, perPage: Int):   List<User> {
        return apiService.reqUsers(since, perPage);
    }

}


interface PagingApiService {


    @GET("users")
    suspend fun reqUsers(@Query("since") since: Int, @Query("per_page") perPage: Int): List<User>
}