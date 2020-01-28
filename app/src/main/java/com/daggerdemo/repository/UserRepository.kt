package com.daggerdemo.repository

import com.daggerdemo.models.Post
import com.daggerdemo.utils.extensions.safeApiCall
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import com.daggerdemo.utils.state.Result

/**
 * Created by Manish Patel on 1/28/2020.
 */
// @Inject lets Dagger know how to create instances of this object

@Singleton
class UserRepository @Inject constructor(
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