package com.expdemo.data

import com.expdemo.models.Post
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface RetrofitApiService {

    /** Testing API call**/
    @GET("typicode/demo/posts")
    suspend fun getPosts(): Response<List<Post>>

    /** Testing API call**/
    @GET("typicode/demo/posts")
    fun getPostsWithAwait(): Call<List<Post>>

    /** Testing API call**/
    @GET("typicode/demo/posts")
    fun getPostsWithLiveData(): List<Post>
}