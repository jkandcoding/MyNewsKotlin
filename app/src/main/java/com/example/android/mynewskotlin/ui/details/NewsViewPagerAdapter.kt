package com.example.android.mynewskotlin.ui.details

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.android.mynewskotlin.data.NewsArticle


class NewsViewPagerAdapter(
    val articles: List<NewsArticle>,
    fm: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fm, lifecycle) {

    override fun getItemCount(): Int {
        return articles.size
    }

    override fun createFragment(pos: Int): Fragment {
        val article = articles[pos]
        return OneArticleFragment(article)
    }


}