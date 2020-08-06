package com.blepoc.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy

/**
 * Created by Manish Patel on 8/6/2020.
 */
@Dao
interface BLEStore {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bleEntry: BLEEntry): Long

}