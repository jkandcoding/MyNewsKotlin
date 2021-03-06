package com.example.android.mynewskotlin.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import org.threeten.bp.OffsetDateTime

@Parcelize
@Entity(tableName = "news")
data class NewsArticle(
    val author: String?,
    @PrimaryKey
    val title: String,
    val description: String,
    val urlToImage: String,
    val publishedAt: String,
    val timestamp: OffsetDateTime
) : Parcelable
