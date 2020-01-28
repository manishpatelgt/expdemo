package com.daggerdemo.repository

import androidx.room.*
import com.daggerdemo.models.Post

@Dao
interface PostDao {

    @Insert
    suspend fun insert(post: Post)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<Post>)

    @Delete
    suspend fun delete(post: Post)

    @Query("DELETE FROM tbl_Post")
    suspend fun deleteAll()

}