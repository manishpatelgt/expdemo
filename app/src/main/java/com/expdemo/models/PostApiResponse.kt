package com.expdemo.models

import com.expdemo.utils.constants.FieldConstants
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PostApiResponse(

    @Json(name = FieldConstants.POST)
    val posts: List<Post>
)