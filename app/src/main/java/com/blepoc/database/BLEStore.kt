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

    @Query("SELECT * FROM tblDeviceLog WHERE DeviceId=:deviceId LIMIT 1")
    suspend fun getDevice(deviceId: String): BLEEntry

    @Query("DELETE FROM tblDeviceLog WHERE DeviceId=:deviceId")
    suspend fun removeDevice(deviceId: String)

    @Query("DELETE FROM tblDeviceLog")
    suspend fun clearLogs()

    @Query("UPDATE tblDeviceLog SET IsAlert = 1 WHERE Mac=:mac")
    suspend fun updateAlertFlag(mac: String)

    @Query("UPDATE tblDeviceLog SET LastVisibleTimeStamp = :lastVisibletimeStamp WHERE DeviceId=:deviceId ")
    suspend fun updateLastVisibleTimestamp(lastVisibletimeStamp: Long, deviceId: String)
}