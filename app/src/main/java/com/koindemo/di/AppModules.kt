package com.koindemo.di

import com.facebook.stetho.okhttp3.StethoInterceptor
import com.koindemo.api.PostApi
import com.koindemo.db.PostDatabase
import com.koindemo.repository.PostRepository
import com.koindemo.ui.activity.MainViewModel
import com.koindemo.ui.fragments.MainFragment
import com.koindemo.ui.fragments.MainFragmentViewModel
import com.koindemo.utils.constants.Constants
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.koin.android.viewmodel.dsl.viewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Manish Patel on 4/1/2020.
 */

/** DATABASE MODULES ------------------------------------------------------------------------------------------------ */
val databaseModule = module {

    /** Database */
    single {
        PostDatabase.getDatabase(androidApplication())
    }

    /** Post DAO */
    factory {
        get<PostDatabase>().getPostDao()
    }
}


/** NETWORK MODULES ------------------------------------------------------------------------------------------------- */
val networkModule = module {

    /** WEB SERVICE - REST API */
    single {
        createWebService<PostApi>()
    }

    /**
     * Repository
     * Single source of truth
     * */
    single {
        PostRepository(get(), get())
    }
}

/** VIEWMODEL MODULES ------------------------------------------------------------------------------------------------- */
val viewModelModule = module {
    viewModel {
        MainViewModel(get())
    }

    viewModel {
        MainFragmentViewModel()
    }
}

/** FRAMGNETS MODULES ------------------------------------------------------------------------------------------------- */
val fragmentModule = module {
    /** MainFragment */
    factory {
        MainFragment()
    }

}
/** 3.KOIN APP MODULES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

val appModule = listOf(networkModule, databaseModule, viewModelModule, fragmentModule)


/** 4.FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

/** WEB SERVICE & REST API ------------------------------------------------------------------------------------------ */
inline fun <reified T> createWebService(): T {
    val retrofit = provideRetrofit()
    return retrofit.create(T::class.java)
}

/** RETROFIT -------------------------------------------------------------------------------------------------------- */
fun provideRetrofit(): Retrofit {
    return Retrofit.Builder()
        .baseUrl(Constants.API_URL)
        .client(
            provideOkHttpClient(
                provideLoggingInterceptor(),
                Constants.CONNECT,
                Constants.READ,
                Constants.WRITE
            )
        )
        .addConverterFactory(MoshiConverterFactory.create(provideMoshiBuilder()))
        //.addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .build()
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

/** OKHTTP LOGGING -------------------------------------------------------------------------------------------------- */
fun provideLoggingInterceptor(): HttpLoggingInterceptor {
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    return interceptor
}