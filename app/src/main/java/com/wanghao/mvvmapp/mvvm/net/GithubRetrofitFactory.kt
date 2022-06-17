package com.wanghao.mvvmapp.mvvm.net

import androidx.compose.ui.platform.LocalContext
import com.google.gson.GsonBuilder
import com.readystatesoftware.chuck.ChuckInterceptor
import com.wanghao.mvvmapp.BaseApplication
import com.wanghao.mvvmapp.mvvm.encrypt.Const
import okhttp3.ConnectionPool
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit


/**
 * Created with Android Studio.
 * Description:
 * @date: 2020/02/24
 * Time: 16:56
 */

class GithubRetrofitFactory private constructor() {

    companion object {
        //超时时间
        private const val DEFAULT_CONNECT_TIMEOUT = 60L
        private const val DEFAUTL_WRITE_TIMEOUT = 60L
        private const val DEFAULT_READ_TIMEOUT = 60L
        val instance by lazy {
            GithubRetrofitFactory()
        }
    }

    fun <Service> getService(serviceClass: Class<Service>, baseUrl: String = Const.URL_GITHUB_TEST): Service {
        return Retrofit.Builder()
            .client(initOkHttpClient())
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder().disableHtmlEscaping().create()
                )
            )
            .baseUrl(baseUrl)
            .build()
            .create(serviceClass)
    }

    private fun initOkHttpClient(): OkHttpClient {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder()
            .sslSocketFactory(
                SSLSocketClient.getSSLSocketFactory(),
                SSLSocketClient.getTrustManager()
            )
            .hostnameVerifier(SSLSocketClient.getHostnameVerifier())
            .addInterceptor(ChuckInterceptor(BaseApplication.context))
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(
                DEFAULT_CONNECT_TIMEOUT,
                TimeUnit.SECONDS
            )
            .readTimeout(
                DEFAULT_READ_TIMEOUT,
                TimeUnit.SECONDS
            )
            .writeTimeout(
                DEFAUTL_WRITE_TIMEOUT,
                TimeUnit.SECONDS
            )
            .proxy(Proxy.NO_PROXY)
            .connectionPool(ConnectionPool(8, 15, TimeUnit.SECONDS))
        return builder.build()
    }

}