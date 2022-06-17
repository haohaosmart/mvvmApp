package com.wanghao.mvvmapp.mvvm.view

import android.content.res.Resources
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.wanghao.mvvmapp.mvvm.utils.ActivityStackManager
import com.wanghao.mvvmapp.mvvm.utils.AndroidBugFixUtils
import com.wanghao.mvvmapp.mvvm.utils.BindingReflex
import com.wanghao.mvvmapp.mvvm.utils.network.AutoRegisterNetListener
import com.wanghao.mvvmapp.mvvm.utils.network.NetworkStateChangeListener
import com.wanghao.mvvmapp.mvvm.utils.network.NetworkTypeEnum
import com.wanghao.mvvmapp.mvvm.viewModel.BaseViewModel

/**
 * Activity基类
 *
 * @author Qu Yunshuo
 * @since 8/27/20
 */
abstract class BaseActivity<VB : ViewBinding, VM : BaseViewModel> : AppCompatActivity(),
    FrameView<VB>, NetworkStateChangeListener {

    protected val mBinding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        BindingReflex.reflexViewBinding(javaClass, layoutInflater)
    }

    protected abstract val mViewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(mBinding.root)


        setStatusBar()
        mBinding.initView()
        initNetworkListener()
        initObserve()
        initRequestData()
    }
    override fun onResume() {
        super.onResume()
        Log.d("ActivityLifecycle", "ActivityStack: ${ActivityStackManager.activityStack}")
    }
    /**
     * 初始化网络状态监听
     * @return Unit
     */
    private fun initNetworkListener() {
        lifecycle.addObserver(AutoRegisterNetListener(this))
    }

    /**
     * 设置状态栏
     * 子类需要自定义时重写该方法即可
     * @return Unit
     */
    open fun setStatusBar() {
//        BarUtils.transparentStatusBar(this)
//        BarUtils.setStatusBarLightMode(this, true)
    }

    /**
     * 网络类型更改回调
     * @param type Int 网络类型
     * @return Unit
     */
    override fun networkTypeChange(type: NetworkTypeEnum) {}

    /**
     * 网络连接状态更改回调
     * @param isConnected Boolean 是否已连接
     * @return Unit
     */
    override fun networkConnectChange(isConnected: Boolean) {
        Toast.makeText(this,if (isConnected) "网络已连接" else "网络已断开", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {

        super.onDestroy()
        // 解决某些特定机型会触发的Android本身的Bug
        AndroidBugFixUtils().fixSoftInputLeaks(this)
    }


}