package com.daggerdemo.repository

import com.daggerdemo.data.RetrofitApiService
import com.daggerdemo.data.RetrofitFactory
import javax.inject.Inject

class UserRemoteDataSource @Inject constructor() {
    var retrofitApiService: RetrofitApiService = RetrofitFactory.apiService
}