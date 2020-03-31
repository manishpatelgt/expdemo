package com.koindemo.db

import androidx.room.*
import com.koindemo.model.Post

/**
 * Created by Manish Patel on 3/31/2020.
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
