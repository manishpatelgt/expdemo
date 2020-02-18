package com.expdemo.repository

import androidx.lifecycle.LiveData
import androidx.room.*
import com.expdemo.models.Post

@Dao
interface PostDao {

    @Query("SELECT * from tbl_Post")
    fun getAllPosts(): LiveData<List<Post>>

    @Insert
    suspend fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Delete
    suspend fun delete(post: Post)

    @Query("DELETE FROM tbl_Post")
    suspend fun deleteAll()

}