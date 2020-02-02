package com.expdemo.repository

import com.expdemo.data.RetrofitApiService
import com.expdemo.data.RetrofitFactory
import com.expdemo.utils.constants.Constants
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class UserRemoteDataSource {
    var retrofitApiService: RetrofitApiService = RetrofitFactory.apiService
}