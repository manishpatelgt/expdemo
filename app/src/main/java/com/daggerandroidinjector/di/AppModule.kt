package com.daggerandroidinjector.di

import com.daggerandroidinjector.api.PostApi
import com.daggerandroidinjector.db.PostDatabase
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import com.daggerandroidinjector.utils.constants.Constants.API_URL
/**
 * Created by Manish Patel on 2/6/2020.
 */
@Module
class AppModule {

    @Provides
    fun providePostDb(database: PostDatabase) = database.postDao()

    @Provides
    @Singleton
    fun provideApi(): PostApi {
       return getServiceApi(getRetrofit())
    }

    /** WEB SERVICE & REST API ------------------------------------------------------------------------------------------ */
    fun getServiceApi(retrofit: Retrofit) = retrofit.create(PostApi::class.java)

    /** RETROFIT -------------------------------------------------------------------------------------------------------- */
    fun getRetrofit() = Retrofit.Builder()
        .baseUrl(API_URL)
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