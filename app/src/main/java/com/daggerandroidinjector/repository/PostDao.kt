package com.daggerandroidinjector.repository

import androidx.room.*
import com.daggerandroidinjector.model.Post

/**
 * Created by Manish Patel on 2/6/2020.
 */
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