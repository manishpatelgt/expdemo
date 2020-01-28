package com.daggerdemo.data

import com.daggerdemo.utils.constants.Constants.BASE_URL
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitFactory {

    val apiService = getServiceApi(getRetrofit())

    /** WEB SERVICE & REST API ------------------------------------------------------------------------------------------ */
    fun getServiceApi(retrofit: Retrofit) = retrofit.create(RetrofitApiService::class.java)

    /** RETROFIT -------------------------------------------------------------------------------------------------------- */
    fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(provideOkHttpClient(provideLoggingInterceptor(), 30, 30, 30))
        .addConverterFactory(MoshiConverterFactory.create(provideMoshiBuilder()))
        .build()

    /** OKHTTP LOGGING -------------------------------------------------------------------------------------------------- */
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    /** MOSHI ----------------------------------------------------------------------------------------------------------- */
    fun provideMoshiBuilder(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    /** OKHTTP ---------------------------------------------------------------------------------------------------------- */
    fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor,
        connect: Long,
        read: Long,
        write: Long
    ): OkHttpClient {
        val client = OkHttpClient.Builder()
        client.connectTimeout(connect, TimeUnit.SECONDS)
        client.readTimeout(read, TimeUnit.SECONDS)
        client.writeTimeout(write, TimeUnit.SECONDS)
        client.addInterceptor(interceptor)
        client.addNetworkInterceptor(StethoInterceptor())
        return client.build()
    }

}