package com.blepoc.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 * Created by Manish Patel on 8/6/2020.
 */
@Dao
interface BLEStore {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(bleEntry: BLEEntry)

    @Query("SELECT * FROM tblDeviceLog WHERE Mac=:mac LIMIT 1")
    suspend fun getDevice(mac: String): BLEEntry

    @Query("DELETE FROM tblDeviceLog")
    suspend fun clearLogs()

    @Query("UPDATE tblDeviceLog SET IsAlert = 1 WHERE Mac=:mac")
    suspend fun updateAlertFlag(mac: String)

}