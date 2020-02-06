package com.daggerandroidinjector.repository

import com.daggerandroidinjector.api.PostApi
import com.daggerandroidinjector.model.Post
import com.daggerandroidinjector.utils.safeApiCall
import java.io.IOException
import javax.inject.Inject
import com.daggerandroidinjector.utils.state.Result
import javax.inject.Singleton

/**
 * Created by Manish Patel on 2/6/2020.
 */
@Singleton
class PostRepository @Inject constructor(
    private val api: PostApi,
    private val postDao: PostDao
) {

    suspend fun getPosts() = safeApiCall(
        call = { callPostApi() },
        errorMessage = "Something went wrong. Please try again later!"
    )

    suspend fun callPostApi(): Result<List<Post>> {
        val response = api.getPosts()
        if (response.isSuccessful) return Result.Success(response.body()!!)
        return Result.Error(IOException("Something went wrong. Please try again later!"))
    }

    suspend fun insert(post: Post) {
        postDao.insert(post)
    }

    suspend fun insertAll(posts: List<Post>) {
        postDao.insertAll(posts)
    }

    suspend fun deleteAll() {
        postDao.deleteAll()
    }
}