package com.e.carty.webservices

import com.frzah.base.AppController
import com.frzah.utils.SharedPreferenceUtil
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit


object ApiClient {

    fun getApiClient(baseUrl: String, connectTimeoutInSec: Long = 30, readTimeoutInSec: Long = 50, writeTimeoutInSec: Long = 60): ApiInterface {
        return getRetrofitClient(getOkhttpClientBuilder(connectTimeoutInSec, readTimeoutInSec, writeTimeoutInSec), baseUrl).create(ApiInterface::class.java)
    }


    fun getRetrofitClient(okHttpClientBuilder: OkHttpClient.Builder, baseUrl: String) = Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .addConverterFactory(ScalarsConverterFactory.create())
        .client(okHttpClientBuilder.build())
        .baseUrl(baseUrl)
        .build()

    fun getOkhttpClientBuilder(connectTimeoutInSec: Long, readTimeoutInSec: Long, writeTimeoutInSec: Long): OkHttpClient.Builder {
        val okHttpClientBuilder = OkHttpClient.Builder()
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        okHttpClientBuilder.connectTimeout(connectTimeoutInSec, TimeUnit.SECONDS)
        okHttpClientBuilder.readTimeout(readTimeoutInSec, TimeUnit.SECONDS)
        okHttpClientBuilder.writeTimeout(writeTimeoutInSec, TimeUnit.SECONDS)
      okHttpClientBuilder.addInterceptor(object : Interceptor {
            @Throws(IOException::class)
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val original: Request = chain.request()
                val request: Request = original.newBuilder()
                    .header("X-localization", SharedPreferenceUtil.getInstance(AppController.getInstance()!!).selectedLanguage)
                    .method(original.method, original.body)
                    .build()
                return chain.proceed(request)
            }
        })
        okHttpClientBuilder.addInterceptor(loggingInterceptor)
        return okHttpClientBuilder
    }

}