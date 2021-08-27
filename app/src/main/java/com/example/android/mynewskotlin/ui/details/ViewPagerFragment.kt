package com.example.android.mynewskotlin.ui.details

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.android.mynewskotlin.R
import com.example.android.mynewskotlin.data.NewsArticle
import com.example.android.mynewskotlin.databinding.FragmentViewPagerBinding
import com.example.android.mynewskotlin.ui.gallery.NewsViewModel

class ViewPagerFragment : Fragment(R.layout.fragment_view_pager) {

    private val args by navArgs<ViewPagerFragmentArgs>()
    var galleryClick = 0
    private val viewModel: NewsViewModel by activityViewModels()
    private lateinit var newsViewPagerAdapter: NewsViewPagerAdapter
    private lateinit var viewPager: ViewPager2
    private lateinit var viewPager2OnPageChangeCallback: ViewPager2.OnPageChangeCallback

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentViewPagerBinding.bind(view)

        val currentPosition = args.position
        val articles = ArrayList<NewsArticle>()

        viewModel.articlesForViewPager.observe(viewLifecycleOwner) {
            articles.addAll(it)
            newsViewPagerAdapter.notifyDataSetChanged()
        }

        viewPager = binding.viewPager2

        newsViewPagerAdapter =
            NewsViewPagerAdapter(
                articles,
                requireActivity().supportFragmentManager,
                viewLifecycleOwner.lifecycle
            )

        viewPager2OnPageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // sets title for every item but first clicked
                (requireActivity() as AppCompatActivity).supportActionBar?.title =
                    articles[position].title

                if (position == 0 && galleryClick == 0) {
                    viewPager.setCurrentItem(currentPosition, false)
                    viewModel.sendPosition(currentPosition)
                    galleryClick++

                } else if (position == 0 && galleryClick > 1) {
                    viewModel.sendPosition(position)
                    galleryClick++

                } else if (position != 0 && galleryClick > 0) {
                    viewModel.sendPosition(position)
                    galleryClick++
                }
            }
        }

        viewPager.registerOnPageChangeCallback(viewPager2OnPageChangeCallback)
        viewPager.adapter = newsViewPagerAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        viewPager.unregisterOnPageChangeCallback(viewPager2OnPageChangeCallback)
    }

}