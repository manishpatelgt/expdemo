package com.koindemo.repository

import androidx.lifecycle.LiveData
import com.koindemo.api.PostApi
import com.koindemo.db.PostDao
import com.koindemo.model.Post
import com.koindemo.utils.state.Result
import com.koindemo.utils.extensions.handleException
import com.koindemo.utils.extensions.safeApiCall
import retrofit2.HttpException

/**
 * Created by Manish Patel on 3/31/2020.
 */
class PostRepository constructor(
    private val postDao: PostDao,
    private val postApi: PostApi
) {

    suspend fun getPosts() = safeApiCall(
        call = { callPostApi() },
        errorMessage = "Something went wrong. Please try again later!"
    )

    suspend fun callPostApi(): Result<List<Post>> {
        return try {
            val response = postApi.getPosts()
            if (response.isSuccessful) return Result.Success(response.body()!!)
            return Result.Error("Something went wrong. Please try again later!")
        } catch (e: Exception) {
            e.printStackTrace()
            handleException(e)
        }
    }

    suspend fun callPostApi2(): Result<List<Post>> {
        return try {
            val response = postApi.getPosts()
            println("status code: ${response.code()}")
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                when (response.code()) {
                    404 -> Result.Error("Webservice Not found")
                    401 -> Result.Error("Un-authorised")
                    500 -> Result.Error("Server not found")
                    else -> Result.Error("couldn't connect to server")
                }
            }

        } catch (httpException: HttpException) {
            println("error: ${httpException.message}")
            Result.Error(httpException.response()?.errorBody()?.string() ?: httpException.message())
        } catch (t: Throwable) {
            t.printStackTrace()
            println("error: ${t.message}")
            Result.Error(t.localizedMessage)
        }
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

    fun getPostFromDB(): LiveData<List<Post>> {
        return postDao.getAllPosts()
    }
}