package com.expdemo.repository

import com.expdemo.models.Post
import com.expdemo.utils.extensions.safeApiCall
import java.io.IOException
import com.expdemo.utils.state.Result

class UserRepository(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {

    suspend fun getPosts() = safeApiCall(
        call = { callPostApi() },
        errorMessage = "Something went wrong. Please try again later!"
    )

    suspend fun callPostApi(): Result<List<Post>> {
        val retrofitApiService = remoteDataSource.retrofitApiService
        val response = retrofitApiService.getPosts()
        if (response.isSuccessful) return Result.Success(response.body()!!)
        return Result.Error(IOException("Something went wrong. Please try again later!"))
    }

    suspend fun insert(post: Post) {
        localDataSource.postDao.insert(post)
    }

    suspend fun insertAll(posts: List<Post>) {
        localDataSource.postDao.insertAll(posts)
    }

    suspend fun deleteAll() {
        localDataSource.postDao.deleteAll()
    }
}