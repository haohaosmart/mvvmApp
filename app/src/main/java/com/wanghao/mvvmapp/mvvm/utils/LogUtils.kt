package com.wanghao.mvvmapp.mvvm.utils

import android.util.Log
import com.wanghao.mvvmapp.BuildConfig

object LogUtils {
    private const val TAG = "com.zwb.lib_base"

    private var DEBUG = BuildConfig.DEBUG

    fun d(tag:String = TAG, msg: String) {
        if (DEBUG) {
            Log.d(tag, msg)
        }
    }

    fun e(tag:String = TAG,msg: String) {
        if (DEBUG) {
            Log.e(tag, msg)
        }
    }

    fun i(tag:String = TAG,msg: String) {
        if (DEBUG) {
            Log.i(tag, msg)
        }
    }

    fun v(tag:String = TAG,msg: String) {
        if (DEBUG) {
            Log.v(tag, msg)
        }
    }

    fun w(tag:String = TAG,msg: String) {
        if (DEBUG) {
            Log.w(tag, msg)
        }
    }
}