package com.example.android.mynewskotlin.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.android.mynewskotlin.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    // position to scroll to on first screen when going back from second screen
    val scrollToPosition = MutableLiveData<Int>()

    // get articles: PagingData for first screen
    val articles by lazy { repository.getArticles().cachedIn(viewModelScope) }

    // get articles from room db for second screen (viewPager2)
    val articlesForViewPager =
        repository.getArticlesForViewPager()

    fun sendPosition(position: Int) {
        scrollToPosition.value = position
    }
}