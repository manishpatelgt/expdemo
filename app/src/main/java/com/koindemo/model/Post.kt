package com.koindemo.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.koindemo.utils.constants.FieldConstants

/**
 * Created by Manish Patel on 3/31/2020.
 */
@Entity(tableName = "tbl_Post")
data class Post(

    @PrimaryKey
    @ColumnInfo(name = FieldConstants.ID)
    val id: String?= null,

    @ColumnInfo(name = FieldConstants.TITLE)
    var title: String? =null

)