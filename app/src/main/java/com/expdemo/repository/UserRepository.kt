package com.expdemo.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.expdemo.models.Post
import com.expdemo.ui.retrofitdemo.ErrorCodes
import com.expdemo.ui.retrofitdemo.Resource
import com.expdemo.utils.extensions.safeApiCall
import java.io.IOException
import com.expdemo.utils.state.Result
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.net.SocketTimeoutException

class UserRepository(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {

    var retrofitApiService = remoteDataSource.retrofitApiService

    //https://medium.com/corouteam/exploring-kotlin-coroutines-and-lifecycle-architectural-components-integration-on-android-c63bb8a9156f
    //https://proandroiddev.com/suspend-what-youre-doing-retrofit-has-now-coroutines-support-c65bd09ba067
    fun getPostsWithAwait(): List<Post> {
        var list = mutableListOf<Post>()

        retrofitApiService.getPostsWithAwait().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (response.isSuccessful) {
                    // When data is available, populate list
                    val listS = response.body()
                    list.addAll(listS!!)
                }
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                t.printStackTrace()
            }
        })

        return list
    }

    suspend fun getPosts() = safeApiCall(
        call = { callPostApi() },
        errorMessage = "Something went wrong. Please try again later!"
    )

    suspend fun getPostFromDB(): LiveData<List<Post>> {
        return localDataSource.postDao.getAllPosts()
    }

    suspend fun callPostApi(): Result<List<Post>> {
        return try {
            val response = retrofitApiService.getPosts()
            if (response.isSuccessful) return Result.Success(response.body()!!)
            return Result.Error("Something went wrong. Please try again later!")
        } catch (e: Exception) {
            handleException(e)
        }
        /*val response = retrofitApiService.getPosts()
        if (response.isSuccessful) return Result.Success(response.body()!!)
        return Result.Error(IOException("Something went wrong. Please try again later!"))*/
    }

    fun handleException(e: Exception): Result.Error {
        return when (e) {
            is HttpException -> Result.Error(getErrorMessage(e.code()))
            is SocketTimeoutException -> Result.Error(getErrorMessage(ErrorCodes.SocketTimeOut.code))
            else -> Result.Error(getErrorMessage(Int.MAX_VALUE))
        }
    }

    private fun getErrorMessage(code: Int): String {
        return when (code) {
            ErrorCodes.SocketTimeOut.code -> "Timeout"
            401 -> "Unauthorised"
            404 -> "Not found"
            else -> "Something went wrong"
        }
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