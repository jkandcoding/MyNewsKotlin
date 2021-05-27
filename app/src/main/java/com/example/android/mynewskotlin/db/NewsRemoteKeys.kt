package com.example.android.mynewskotlin.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class NewsRemoteKeys(
    @PrimaryKey
    val title: String,
    val prevKey: Int?,
    val nextKey: Int?
) {
}