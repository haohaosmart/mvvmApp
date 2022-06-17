package com.wanghao.mvvmapp.mvvm.ktx

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wanghao.mvvmapp.mvvm.net.BaseResponse
import com.wanghao.mvvmapp.mvvm.net.NetExceptionHandle
import com.wanghao.mvvmapp.mvvm.net.State
import com.wanghao.mvvmapp.mvvm.net.StateType
import com.wanghao.mvvmapp.mvvm.viewModel.BaseViewModel
import kotlinx.coroutines.launch


/**
 * Created with Android Studio.
 * Description:数据解析扩展函数
 * @CreateDate: 2020/4/19 17:35
 */

fun <T> BaseResponse<T>.dataConvert(
    loadState: MutableLiveData<State>,
    urlKey:String = ""
): T {
    return when (code) {
        "200" -> {
            if (data is List<*>) {
                if ((data as List<*>).isEmpty()) {
                    loadState.postValue(State(StateType.EMPTY,urlKey))
                }
            }
            loadState.postValue(State(StateType.SUCCESS,urlKey))
            data
        }
        else -> {
            loadState.postValue(State(StateType.ERROR,urlKey, message = msg))
            data
        }
    }
}


fun BaseViewModel.initiateRequest(
    block: suspend () -> Unit,
    loadState: MutableLiveData<State>,
    urlKey:String=""
) {
    viewModelScope.launch {
        /*run {

        }*/
        runCatching {
            block()
        }.onSuccess {
        }.onFailure {
            NetExceptionHandle.handleException(it, loadState, urlKey)
        }
    }
}
