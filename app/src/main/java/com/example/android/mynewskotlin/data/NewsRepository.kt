package com.example.android.mynewskotlin.data

import androidx.lifecycle.LiveData
import androidx.paging.*
import com.example.android.mynewskotlin.api.NewsApi
import com.example.android.mynewskotlin.db.NewsDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NewsRepository @Inject constructor(
    private val newsApi: NewsApi,
    private val newsDatabase: NewsDatabase
) {
    @OptIn(ExperimentalPagingApi::class)
    fun getArticles(): LiveData<PagingData<NewsArticle>> {
        val pagingSourceFactory = {
            newsDatabase.newsArticleDao().loadNews()
        }

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = true,
                prefetchDistance = 15,
                initialLoadSize = 40
            ),
            remoteMediator = NewsRemoteMediator(
                newsApi,
                newsDatabase
            ),
            pagingSourceFactory = pagingSourceFactory
        ).liveData
    }

    fun getArticlesForViewPager(): LiveData<List<NewsArticle>> {
        return newsDatabase.newsArticleDao().loadNewsForViewPager()
    }
}