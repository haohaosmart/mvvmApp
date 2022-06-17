package com.wanghao.mvvmapp.mvvm.model

class People {
    val name: String? = null
    var age: Int = 0
        //返回field的值
        get() = field
        //设置field的值
        set(value) {
            field = value
        }

    var isAbove18: Boolean = false
        get() = age > 18
}
