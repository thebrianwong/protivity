package com.thebrianwong.protivity.lambda

data class Content (
    val content: String
)

data class ResponseObj(
    val statusCode: Int,
    val body: Content,
)

data class BodyDuration(private val duration: Long)
