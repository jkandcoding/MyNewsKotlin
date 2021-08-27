package com.example.android.mynewskotlin.ui.gallery

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.example.android.mynewskotlin.R
import com.example.android.mynewskotlin.databinding.FragmentGalleryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.fragment_gallery), NewsAdapter.OnItemClickListener {

    private val viewModel: NewsViewModel by activityViewModels()
    private var _binding: FragmentGalleryBinding? = null
    private val binding get() = _binding!! // return nonnullable type
    private val adapter = NewsAdapter(this)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentGalleryBinding.bind(view)

        binding.apply {
            rvGallery.setHasFixedSize(true)
            rvGallery.itemAnimator = null
            rvGallery.adapter = adapter.withLoadStateHeaderAndFooter(
                header = NewsLoadStateAdapter { adapter.retry() },
                footer = NewsLoadStateAdapter { adapter.retry() }
            )

            btnGalleryRetry.setOnClickListener {
                adapter.retry()
            }

            swipeRefreshGallery.setOnRefreshListener {
                adapter.refresh()
            }
        }

        viewModel.articles.observe(viewLifecycleOwner) {
            adapter.submitData(viewLifecycleOwner.lifecycle, it)
        }

        adapter.addLoadStateListener { loadstate ->
            binding.apply {
                rvGallery.isVisible = loadstate.mediator?.refresh is LoadState.NotLoading ||
                        loadstate.source.refresh is LoadState.NotLoading
                btnGalleryRetry.isVisible = loadstate.mediator?.refresh is LoadState.Error
                tvGalleryError.isVisible = loadstate.mediator?.refresh is LoadState.Error
                swipeRefreshGallery.isRefreshing = loadstate.mediator?.refresh is LoadState.Loading
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.scrollToPosition.observe(viewLifecycleOwner) { position ->
            binding.rvGallery.scrollToPosition(position)
        }
        //  adapter.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(position: Int) {
        val action =
            GalleryFragmentDirections.actionGalleryFragmentToDetailsFragment(position)
        findNavController().navigate(action)
    }
}