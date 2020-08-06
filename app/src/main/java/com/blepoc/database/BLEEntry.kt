package com.blepoc.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by Manish Patel on 8/6/2020.
 */
@Entity(tableName = "tblDeviceLog")
data class BLEEntry(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "ID")
    var id: String = "",

    @ColumnInfo(name = "InsertDateTime")
    var insertDateTime: String = "",

    @ColumnInfo(name = "Mac")
    var mac: String = "",

    @ColumnInfo(name = "Name")
    var name: String = ""

)