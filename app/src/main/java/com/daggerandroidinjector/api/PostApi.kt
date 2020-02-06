package com.daggerandroidinjector.api

import com.daggerandroidinjector.model.Post
import retrofit2.Response
import retrofit2.http.GET

/**
 * Created by Manish Patel on 2/6/2020.
 */
interface PostApi {

    /** Testing API call**/
    @GET("typicode/demo/posts2")
    suspend fun getPosts(): Response<List<Post>>
}