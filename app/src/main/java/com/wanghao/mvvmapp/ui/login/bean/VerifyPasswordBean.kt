package com.wanghao.mvvmapp.ui.login.bean

import com.google.gson.annotations.SerializedName


data class VerifyPasswordRequest (
    var longitude: String = "", var latitude: String = "",
    var deviceId: String = "", var areaCode: String = "", var phoneNo: String = "", var passWord: String = "",
    var pwdType: String = "1", var scene: String = "0", var address: String = "",
    var deviceName: String = "", var amount: String = ""
)


data class VerifyPasswordResponse(
    var token: String, var code: String, var msg: String, var flag: String, var times: String, var deviceKey: String,
    var alertContent: String, var orderId: String, val orderSecondType: String, var orderFirstType: String
)


data class VerifyPasswordData(var flag: String, var times: String, var deviceKey: String )