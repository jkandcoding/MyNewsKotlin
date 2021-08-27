package com.example.android.mynewskotlin.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.android.mynewskotlin.R
import com.example.android.mynewskotlin.data.NewsArticle
import com.example.android.mynewskotlin.databinding.FragmentOneArticleBinding

class OneArticleFragment(private val article: NewsArticle) :
    Fragment(R.layout.fragment_one_article) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // sets title for first clicked item, except if position of that item is 0
        (requireActivity() as AppCompatActivity).supportActionBar?.title = article.title

        val binding = FragmentOneArticleBinding.bind(view)

        binding.apply {
            Glide.with(this@OneArticleFragment)
                .load(article.urlToImage)
                .error(R.drawable.ic_error)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbFragmentDetails.isVisible = false
                        return false   // otherwise Glide will not load an image
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pbFragmentDetails.isVisible = false
                        return false
                    }
                })
                .into(ivFragmentDetails)

            tvTitleFragmentDetails.text = article.title
            tvDescriptionFragmentDetails.text = article.description
        }
    }


}
