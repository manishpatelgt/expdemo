package com.daggerdemo.data

import com.daggerdemo.models.Post
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitApiService {

    /** Testing API call**/
    @GET("typicode/demo/posts")
    suspend fun getPosts(): Response<List<Post>>
}