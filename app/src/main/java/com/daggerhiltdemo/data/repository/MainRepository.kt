package com.daggerhiltdemo.data.repository

import com.daggerhiltdemo.data.api.ApiHelper
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun getPosts() = apiHelper.getPosts()

}