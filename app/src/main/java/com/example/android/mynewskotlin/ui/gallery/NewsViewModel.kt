package com.example.android.mynewskotlin.ui.gallery

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.android.mynewskotlin.data.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import org.threeten.bp.OffsetDateTime
import javax.inject.Inject

@HiltViewModel
class NewsViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

     //val articles = repository.getArticles().cachedIn(viewModelScope)
     val articles by lazy { repository.getArticles().cachedIn(viewModelScope) }

}