package com.example.android.mynewskotlin.api

import com.example.android.mynewskotlin.data.NewsArticle

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>
)