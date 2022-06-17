package com.wanghao.mvvmapp.mvvm.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wanghao.mvvmapp.mvvm.net.State

/**
 * ViewModel 基类
 *
 * @since 8/27/20
 */
open class BaseViewModel: ViewModel() {

    val loadState by lazy {
        MutableLiveData<State>()
    }

    override fun onCleared() {
        super.onCleared()
    }
}