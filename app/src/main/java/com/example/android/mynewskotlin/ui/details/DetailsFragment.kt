package com.example.android.mynewskotlin.ui.details

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.android.mynewskotlin.R
import com.example.android.mynewskotlin.databinding.FragmentDetailsBinding

class DetailsFragment : Fragment(R.layout.fragment_details) {

    private val args by navArgs<DetailsFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailsBinding.bind(view)

        binding.apply {
            val article = args.article

            Glide.with(this@DetailsFragment)
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
                        tvTitleFragmentDetails.isVisible = true
                        tvDescriptionFragmentDetails.isVisible = true
                        return false
                    }
                })  // we wont to load a drawable into imageView
                .into(ivFragmentDetails)

            tvTitleFragmentDetails.text = article.title
            tvDescriptionFragmentDetails.text = article.description

            // dinamicly changing label, I did it with passing article.title: String via safeArgs
            // (requireActivity() as AppCompatActivity).supportActionBar?.title = article.title

        }
    }
}