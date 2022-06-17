package com.wanghao.mvvmapp.mvvm.net

import android.content.Context
import android.util.Log
import com.wanghao.mvvmapp.mvvm.encrypt.AESUtil
import com.wanghao.mvvmapp.mvvm.encrypt.RSASignatureUtil
import com.wanghao.mvvmapp.mvvm.utils.GsonUtil
import okhttp3.Interceptor
import okhttp3.Response

import okhttp3.ResponseBody.Companion.toResponseBody
import java.nio.charset.Charset


open class ResponseDecryptInterceptor() : Interceptor {

    val TAG = "ResponseInt"


    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val requestContentType = (request.body?.contentType() ?: "").toString().toLowerCase()
        Log.d( TAG,"---requestContentType---$requestContentType")
        var response = chain.proceed(request)

        if (response.isSuccessful) {
            val responseBody = response.body
            if (responseBody != null) {
                /*开始解密*/
                try {
                    val source = responseBody.source()
                    source.request(java.lang.Long.MAX_VALUE)
                    val buffer = source.buffer()
                    var charset = Charset.forName("UTF-8")
                    val contentType = responseBody.contentType()
                    if (contentType != null) {
                        charset = contentType.charset(charset)
                    }
                    val bodyString = buffer.clone().readString(charset)
                    Log.d( TAG,"未解密前的数据---" + bodyString)
                    Log.d(TAG,"requestUrl---${request.url}")

                    if (!requestContentType.contains("multipart")) {
                        val baseResponse: BaseEncryptResponse =
                            GsonUtil.fromJson(bodyString, BaseEncryptResponse::class.java)
                        // LogUtils.d( "验签后的结果----" + baseResponse.checkSign())
                        // 解密后的数据
                        val afterEncryptData = baseResponse.afterDecryptData.trim()
                        Log.d( TAG,"url--${request.url}解密后的业务部分的数据----$afterEncryptData")

                        val newResponseBody = afterEncryptData.toResponseBody(contentType)

                        response = response.newBuilder().body(newResponseBody).build()
                    } else {
                        Log.d( TAG,"---解析上传文件的回文----")
                        val afterEncryptData = AESUtil.getInstance().decrypt(bodyString);
                        Log.d( TAG,"解密后的业务部分的数据----$afterEncryptData")

                        val newResponseBody = afterEncryptData.toResponseBody(contentType)

                        response = response.newBuilder().body(newResponseBody).build()
                    }
                } catch (e: Exception) {
                    /*异常说明解密失败 信息被篡改 直接返回即可 */
                    Log.d( TAG,"解密异常$e")
                    return response
                }
            } else {
                Log.d(TAG, "responseBody为空")
            }
        }
        return response
    }


}


class BaseEncryptResponse {
    var sign: String? = null
    var data: String? = null
    @Throws(java.lang.Exception::class)
    fun checkSign(): Boolean {
        return RSASignatureUtil.getInstance().verify(data, sign)
    }

    @get:Throws(java.lang.Exception::class)
    val afterDecryptData: String
        get() = AESUtil.getInstance().decrypt(data)

}

