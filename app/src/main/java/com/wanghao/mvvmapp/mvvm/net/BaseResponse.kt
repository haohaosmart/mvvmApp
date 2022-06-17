package com.wanghao.mvvmapp.mvvm.net

/**
 * Created with Android Studio.
 * Description: 返回数据基类
 * @date: 2020/02/24
 * Time: 16:04
 */

open class BaseResponse<T>(var data: T, var code: String = "", var msg: String = "")