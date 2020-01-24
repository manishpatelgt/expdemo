package com.expdemo.repository

import com.expdemo.data.RetrofitApiService
import com.expdemo.data.RetrofitFactory

class UserRemoteDataSource {
    var retrofitApiService: RetrofitApiService = RetrofitFactory.apiService
}