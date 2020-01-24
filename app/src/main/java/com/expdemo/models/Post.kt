package com.expdemo.models

import com.expdemo.utils.constants.FieldConstants
import com.squareup.moshi.Json

data class Post(
    @Json(name = FieldConstants.ID)
    val id: String,

    @Json(name = FieldConstants.TITLE)
    val title: String
)