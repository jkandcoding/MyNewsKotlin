package com.example.android.mynewskotlin.data

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.android.mynewskotlin.api.NewsApi
import com.example.android.mynewskotlin.db.NewsDatabase
import com.example.android.mynewskotlin.db.NewsRemoteKeys
import org.threeten.bp.Duration
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.ZoneOffset
import retrofit2.HttpException
import java.io.IOException

private const val NEWS_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    // private val title: String,
    private val newsApi: NewsApi,
    private val newsDatabase: NewsDatabase
) : RemoteMediator<Int, NewsArticle>() {

    private val newsArticleDao = newsDatabase.newsArticleDao()
    private val newsRemoteKeysDao = newsDatabase.newsRemoteKeysDao()

    override suspend fun initialize(): InitializeAction {
        Log.d("hghhgh", "sad sam u INITIALIZE():")
        return if (dbDataOlderThanFive()) {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        } else {
            InitializeAction.SKIP_INITIAL_REFRESH
        }
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, NewsArticle>
    ): MediatorResult {

        return try {
            val loadKey = when (loadType) {
                LoadType.REFRESH -> {
                    Log.d("hghhgh", "sad sam u REFRESH-U")
                    if (dbDataOlderThanFive()) {
                        NEWS_STARTING_PAGE_INDEX
                    } else
                        return MediatorResult.Success(endOfPaginationReached = true)
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                // Query remoteKeyDao for the next RemoteKey.
                LoadType.APPEND -> {

                    val remoteKeyOfLastItemInDb = newsRemoteKeysDao.remoteKeysForLastEntry()

                    if (remoteKeyOfLastItemInDb.nextKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKeyOfLastItemInDb.nextKey
                }
            }

            Log.d("hghhgh", "loadKey is: " + loadKey)
            val response = newsApi.searchNews("bbc-news", loadKey, state.config.pageSize)

            Log.d("hghhgh", "response status: " + response.status)
            var count = 0
            var datum = OffsetDateTime.now()
            newsDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsRemoteKeysDao.clearRemoteKeysDb()
                    newsArticleDao.clearNewsDb()
                }

                val prevKey = if (loadKey == NEWS_STARTING_PAGE_INDEX) null else loadKey - 1
                val nextKey = if (response.articles.isEmpty()) null else loadKey + 1
                val remoteKeys = response.articles.map { newsArticle ->
                    NewsRemoteKeys(
                        title = newsArticle.title,
                        prevKey,
                        nextKey
                    )
                }
                val articlesForDb = response.articles.map { newsArticle ->
                    NewsArticle(
                        author = newsArticle.author,
                        title = newsArticle.title,
                        description = newsArticle.description,
                        urlToImage = newsArticle.urlToImage,
                        publishedAt = newsArticle.publishedAt,
                        timestamp = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC)
                    )
                }

                newsRemoteKeysDao.insertAll(remoteKeys)
                newsArticleDao.insertAll(articlesForDb)
                datum = newsArticleDao.loadLastTimestamp()
                count = newsArticleDao.loadNumberOfArticles()
            }
            Log.d("hghhgh", "zapisa u bazi: " + count + ", totalResults: " + response.totalResults)
            Log.d("hghhgh", "zadnji timestamp: " + datum)
            MediatorResult.Success(endOfPaginationReached = response.totalResults == count)

        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    suspend fun dbDataOlderThanFive(): Boolean {
        val nowTime = OffsetDateTime.now().withOffsetSameLocal(ZoneOffset.UTC)
        val lastTimestamp: OffsetDateTime? = newsArticleDao.loadLastTimestamp()

        return lastTimestamp == null || Duration.between(lastTimestamp, nowTime).toMinutes() > 4
    }
}