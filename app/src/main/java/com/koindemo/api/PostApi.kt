package com.koindemo.api

import com.koindemo.model.Post
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Manish Patel on 3/31/2020.
 */
interface PostApi {

    /** Testing API call**/
    @GET("typicode/demo/posts")
    suspend fun getPosts(): Response<List<Post>>
}