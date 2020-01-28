package com.daggerdemo.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.expdemo.utils.constants.FieldConstants
import com.squareup.moshi.Json

@Entity(tableName = "tbl_Post")
data class Post(

    @PrimaryKey
    @Json(name = FieldConstants.ID)
    val id: String,

    @ColumnInfo(name = FieldConstants.TITLE)
    @Json(name = FieldConstants.TITLE)
    val title: String
)