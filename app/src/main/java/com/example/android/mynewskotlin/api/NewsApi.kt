package com.example.android.mynewskotlin.api

import com.example.android.mynewskotlin.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface NewsApi {

    companion object {
        const val BASE_URL = "https://newsapi.org/"
        const val API_KEY = BuildConfig.NEWSORG_ACCESS_KEY
        const val SOURCES = "bbc-news"
    }

    @Headers("X-Api-Key: $API_KEY")
    @GET("v2/everything")
    suspend fun searchNews(
        @Query("sources") sources: String? = null,
        @Query("page") page: Int? = null,
        @Query("pageSize") pageSize: Int
    ): NewsResponse
}