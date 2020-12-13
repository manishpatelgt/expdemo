package com.daggerhiltdemo.data.api

import com.daggerhiltdemo.data.model.Post
import retrofit2.Response

interface ApiHelper {

    suspend fun getPosts(): Response<List<Post>>
}