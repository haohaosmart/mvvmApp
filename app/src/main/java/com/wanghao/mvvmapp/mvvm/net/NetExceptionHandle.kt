package com.wanghao.mvvmapp.mvvm.net

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonParseException
import retrofit2.HttpException
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Created with Android Studio.
 * Description:
 * @CreateDate: 2020/5/5 11:32
 */
object NetExceptionHandle {
    fun handleException(e: Throwable?, loadState: MutableLiveData<State>,  urlKey:String=""){
        e?.let {
            when (it) {
                is HttpException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR,urlKey))
                }
                is ConnectException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR,urlKey))
                }
                is UnknownHostException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR,urlKey))
                }
                is JsonParseException -> {
                    loadState.postValue(State(StateType.NETWORK_ERROR,urlKey))
                }
            }
        }
    }
}