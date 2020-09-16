package com.blepoc.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Manish Patel on 8/6/2020.
 */
@Entity(tableName = "tblDeviceLog")
data class BLEEntry(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ID")
    var id: Int = 0,

    @ColumnInfo(name = "DeviceId")
    var deviceId: String = "",

    @ColumnInfo(name = "Mac")
    var mac: String? = null,

    @ColumnInfo(name = "Name")
    var name: String? = "",

    @ColumnInfo(name = "InsertDateTime")
    var insertDateTime: String? = null,

    @ColumnInfo(name = "TimeStamp")
    var timeStamp: Long = 0,

    @ColumnInfo(name = "IsAlert")
    var isAlert: Boolean = false,

    @ColumnInfo(name = "LastVisibleTimeStamp")
    var lastVisibleTimeStamp: Long = 0

) {
}