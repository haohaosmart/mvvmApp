package com.wanghao.mvvmapp.ui.paging

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeCompilerApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import coil.compose.AsyncImage
import com.wanghao.mvvmapp.ui.loginCompose.ComposeLoginViewModel
import com.wanghao.mvvmapp.ui.paging.bean.User
import com.wanghao.mvvmapp.ui.widget.ErrorPage
import com.wanghao.mvvmapp.ui.widget.LoadingPage
import com.wanghao.mvvmapp.ui.widget.NextPageLoadError

class PagingForUserActivity : ComponentActivity() {
    val TAG = "PagingUser"
    private val mViewModel by viewModels<PagingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RefreshContentView()
        }
    }


    @Composable
    fun RefreshContentView() {
        val collectAsLazyPagingIDataList = mViewModel.userList.collectAsLazyPagingItems()

        // 首次加载业务逻辑
        when (collectAsLazyPagingIDataList.loadState.refresh) {
            is LoadState.NotLoading -> {
                ContentListView(
                    collectAsLazyPagingIDataList = collectAsLazyPagingIDataList,
                )
            }
            is LoadState.Error -> ErrorPage() { collectAsLazyPagingIDataList.refresh() }
            is LoadState.Loading -> LoadingPage()
        }
    }


    @Composable
    fun ContentListView(
        collectAsLazyPagingIDataList: LazyPagingItems<User>
    ) {

        LazyColumn {


            itemsIndexed(collectAsLazyPagingIDataList){index, value ->
                UserItem(user = value)
            }

            // 加载下一页业务逻辑
            when (collectAsLazyPagingIDataList.loadState.append) {
                is LoadState.NotLoading -> {
                    itemsIndexed(collectAsLazyPagingIDataList) { index, user ->
                       UserItem(user = user)
                    }
                }
                is LoadState.Error -> item {
                    NextPageLoadError {
                        collectAsLazyPagingIDataList.retry()
                    }
                }
                LoadState.Loading -> item {
                    LoadingPage()
                }
            }
        }
    }


    @Composable
    fun UserItem(user: User?) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 5.dp, top = 5.dp, end = 5.dp, bottom = 5.dp)
                .clickable(onClick = {

                }),
            shape = RoundedCornerShape(15.dp),
            elevation = 12.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colors.surface)
                    .padding(5.dp)
            ) {
                    AsyncImage(
                        model = user?.avatar_url,
                        contentDescription = null,
                        modifier = Modifier.size(width = 100.dp, height = 100.dp)
                    )
                user?.login?.let { Text(text = it) }
            }
        }
    }
}