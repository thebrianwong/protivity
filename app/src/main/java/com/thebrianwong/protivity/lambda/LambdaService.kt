package com.thebrianwong.protivity.lambda

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

class LambdaService(url: String, private val apiKey: String) {
    private var lambdaService: LambdaService? = null
    init {
        val retrofit = Retrofit.Builder().baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        lambdaService = retrofit.create(LambdaService::class.java)
    }

    suspend fun generateContent(bodyDuration: BodyDuration): ResponseObj? {
        return lambdaService?.generateContent(apiKey, bodyDuration)
    }

    interface LambdaService {
        @Headers(
            "Content-Type: application/json",
        )
        @POST("protivity")
        suspend fun generateContent(@Header("x-api-key") apiKey: String, @Body bodyDuration: BodyDuration): ResponseObj
    }
}

data class Content (
    val content: String
)

data class ResponseObj(
    val statusCode: Int,
    val body: Content,
)

data class BodyDuration(private val duration: Long)
