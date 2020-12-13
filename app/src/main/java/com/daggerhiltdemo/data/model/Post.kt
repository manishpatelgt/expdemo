package com.daggerhiltdemo.data.model

import com.daggerhiltdemo.utils.FieldConstants
import com.squareup.moshi.Json

data class Post(

    @Json(name = FieldConstants.ID)
    val id: String,

    @Json(name = FieldConstants.TITLE)
    val title: String
)