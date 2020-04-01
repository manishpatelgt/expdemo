package com.koindemo.repository

import androidx.lifecycle.LiveData
import com.koindemo.api.PostApi
import com.koindemo.db.PostDao
import com.koindemo.model.Post
import com.koindemo.utils.state.Result
import com.koindemo.utils.extensions.handleException
import com.koindemo.utils.extensions.safeApiCall

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
            handleException(e)
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