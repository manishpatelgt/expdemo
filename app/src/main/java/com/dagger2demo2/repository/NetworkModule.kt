package com.dagger2demo2.repository

import com.dagger2demo2.data.RetrofitApiService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Manish Patel on 1/29/2020.
 */
// @Module informs Dagger that this class is a Dagger Module
@Module
class NetworkModule {

    // @Provides tell Dagger how to create instances of the type that this function
    // returns (i.e. LoginRetrofitService).
    // Function parameters are the dependencies of this type.
    @Singleton
    @Provides
    fun provideRetrofitService(): RetrofitApiService {
        // Whenever Dagger needs to provide an instance of type LoginRetrofitService,
        // this code (the one inside the @Provides method) is run.
        return Retrofit.Builder()
            .baseUrl("https://example.com")
            .build()
            .create(RetrofitApiService::class.java)
    }
}