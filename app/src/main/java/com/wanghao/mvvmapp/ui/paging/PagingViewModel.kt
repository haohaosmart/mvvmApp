package com.wanghao.mvvmapp.ui.paging

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.wanghao.mvvmapp.mvvm.viewModel.BaseViewModel
import com.wanghao.mvvmapp.ui.login.LoginRepo
import kotlinx.coroutines.flow.cache

class PagingViewModel : BaseViewModel() {

    private val pagingRepo by lazy { PagingRepo() }

    val userList = Pager(
        config = PagingConfig(
            pageSize = 10,
            initialLoadSize = 10, // 第一次加载数量，如果不设置的话是 pageSize * 2
            prefetchDistance = 2,
        )
    ) {
        UserPagingSource(pagingRepo)
    }.flow.cachedIn(viewModelScope)
}