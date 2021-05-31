package com.example.android.mynewskotlin.db

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.android.mynewskotlin.data.NewsArticle
import org.threeten.bp.OffsetDateTime

@Dao
interface NewsArticleDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(articles: List<NewsArticle>)

    @Query("SELECT * FROM news")
    fun loadNews(): PagingSource<Int, NewsArticle>

    @Query("SELECT timestamp FROM news ORDER BY datetime(timestamp) DESC LIMIT 1")
    suspend fun loadLastTimestamp(): OffsetDateTime?

    @Query("SELECT count(*) FROM news")
    suspend fun loadNumberOfArticles(): Int

    @Query("DELETE FROM news")
    suspend fun clearNewsDb()
}