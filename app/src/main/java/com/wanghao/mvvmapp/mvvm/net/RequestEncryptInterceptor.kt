package com.wanghao.mvvmapp.mvvm.net


import android.util.Log
import com.wanghao.mvvmapp.mvvm.encrypt.AESUtil
import com.wanghao.mvvmapp.mvvm.encrypt.RSASignatureUtil
import com.wanghao.mvvmapp.mvvm.utils.GsonUtil
import okhttp3.Interceptor
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject
import java.nio.charset.Charset
import java.util.*


open class RequestEncryptInterceptor : Interceptor {


    val TAG = "ReqInte"


    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        var charset = Charset.forName("UTF-8")
        val method = request.method.toLowerCase().trim()

        val url = request.url
        Log.d(TAG, "---本次请求的url---$url")


        /*如果请求方式是Get或者Delete，此时请求数据是拼接在请求地址后面的*/

        //不是Get和Delete请求时，则请求数据在请求体中
        val requestBody = request.body

        /*判断请求体是否为空  不为空则执行以下操作*/
        if (requestBody != null) {
            val contentType = requestBody.contentType()
            if (contentType != null) {
                charset = contentType.charset(charset)
                /*如果是二进制上传  则不进行加密*/
                if (contentType.type.toLowerCase().equals("multipart")) {
                    return chain.proceed(request)
                }
            }

            val buffer = Buffer()
            requestBody.writeTo(buffer)
            val requestData = buffer.readString(charset).trim()
            val jsonObject = JSONObject(requestData)
            jsonObject.put("token", "")
            jsonObject.put("device", "1")

            jsonObject.put("language","zh")
            jsonObject.put("zoneId", TimeZone.getDefault().id)
            jsonObject.put("sysVersion",android.os.Build.VERSION.RELEASE)
            jsonObject.put("deviceModel", android.os.Build.MODEL)
            jsonObject.put("deviceId",  "12321")
            jsonObject.put("deviceName",  "testDeviceName")
            val jsonObjectPointAddress = JSONObject()
            jsonObjectPointAddress.put("latitude","37")
            jsonObjectPointAddress.put("longitude", "108")
            jsonObjectPointAddress.put("addressLines", "sajfljlasjf")
            jsonObject.put("pointAddress",jsonObjectPointAddress)
            val encryptData = BaseEncryptRequest(jsonObject.toString()).toString()
            Log.d(TAG,"url---$url---加密后 增加tokne和device后的数据---$encryptData")
            /*构建新的请求体*/
            val newRequestBody = encryptData.toRequestBody(contentType)

            /*构建新的requestBuilder*/
            val newRequestBuilder = request.newBuilder()
            //根据请求方式构建相应的请求
            when (method) {
                "post" -> newRequestBuilder.post(newRequestBody)
                "put" -> newRequestBuilder.put(newRequestBody)
            }
            request = newRequestBuilder.build()
        }

        return chain.proceed(request)
    }
}


class BaseEncryptRequest(unEncryptPostBody: String) {
    var sign: String
    var data: String
    val TAG = "BaseEncryptRequest"
    override fun toString(): String {
        return GsonUtil.toJsonString(this)
    }

    init {
        Log.d(TAG,"---unEncryptPostBody---$unEncryptPostBody")
        data = AESUtil.getInstance().encrypt(unEncryptPostBody)
        sign = RSASignatureUtil.getInstance().sign(data)
    }
}
