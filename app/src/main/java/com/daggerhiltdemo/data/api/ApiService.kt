package com.daggerhiltdemo.data.api

import com.daggerhiltdemo.data.model.Post
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    /** Testing API call**/
    @GET("typicode/demo/posts")
    suspend fun getPosts(): Response<List<Post>>
}