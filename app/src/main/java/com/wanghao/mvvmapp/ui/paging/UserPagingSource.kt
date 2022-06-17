package com.wanghao.mvvmapp.ui.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.wanghao.mvvmapp.ui.paging.bean.User
import kotlinx.coroutines.delay

class UserPagingSource(private val repository: PagingRepo) : PagingSource<Int, User>() {

    private val TAG = "pagingSource"

    override fun getRefreshKey(state: PagingState<Int, User>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, User> {

        return try {
            val currentPage = params.key ?: 0
            val pageSize = params.loadSize
            Log.d(TAG, "currentPage: $currentPage")
            Log.d(TAG, "pageSize: $pageSize")

            delay(3000)
            val responseList = repository.reqUsers(since = currentPage, perPage = pageSize)

            // 加载分页
            val preKey = if (currentPage == 0) null else currentPage.minus(1) // 前一个请求的分页条码
            val nextKey: Int? = if (responseList.isNotEmpty()) currentPage + 1 else null // 后一个请求的分页条码  如果当前请求列表数量存在  +1
            Log.d(TAG, "currentPage: $currentPage")
            Log.d(TAG, "preKey: $preKey")
            Log.d(TAG, "nextKey: $nextKey")

            // 回调到最终数据源
            LoadResult.Page(
                data = responseList,
                prevKey = preKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            e.printStackTrace()
            LoadResult.Error(e)
        }
    }
}