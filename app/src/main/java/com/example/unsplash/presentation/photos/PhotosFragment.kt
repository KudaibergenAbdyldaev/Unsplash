package com.example.unsplash.presentation.photos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplash.R
import com.example.unsplash.databinding.FragmentPhotosBinding
import com.example.unsplash.domain.model.UnsplashError
import com.example.unsplash.presentation.adapter.LoaderStateAdapter
import com.example.unsplash.presentation.adapter.OnClickListener
import com.example.unsplash.presentation.adapter.PhotosAdapter
import com.example.unsplash.presentation.detail.DetailFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding: FragmentPhotosBinding
        get() = _binding ?: throw RuntimeException("FragmentPhotosBinding is null")

    private var photosAdapter: PhotosAdapter? = null

    private var connection = true

    private val viewModel: PhotosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setRecyclerViewData()
        checkNetworkConnection()
        setSwipeRefresh()
    }

    private fun checkNetworkConnection() {
        viewModel.connectionLiveData.observe(viewLifecycleOwner) {
            connection = it
            if (it) photosAdapter?.refresh()
            else showNoInternConnection()
        }
    }


    private fun setRecyclerViewData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.photos.collectLatest {
                    photosAdapter?.submitData(viewLifecycleOwner.lifecycle, pagingData = it)
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        val headerAdapter = LoaderStateAdapter()
        val footerAdapter = LoaderStateAdapter()

        photosAdapter = PhotosAdapter(onClickListener = OnClickListener {
            openPhotosDetail(it)
        })
        val concatAdapter = photosAdapter?.withLoadStateHeaderAndFooter(
            header = headerAdapter, footer = footerAdapter
        )
        photosAdapter?.addLoadStateListener { loadState ->
            when {

                loadState.refresh is LoadState.NotLoading && photosAdapter?.itemCount == 0 -> {
                    binding.recyclerView.isVisible = false
                    binding.tvMessage.isVisible = true
                }

                loadState.refresh is LoadState.NotLoading
                        && (photosAdapter?.itemCount ?: 0) > 0 -> {
                    binding.recyclerView.isVisible = true
                    binding.tvMessage.isVisible = false
                }

                loadState.prepend is LoadState.Error -> {
                    val error = loadState.prepend as LoadState.Error
                    initError(error.error)
                }

                loadState.append is LoadState.Error -> {
                    val error = loadState.append as LoadState.Error
                    initError(error.error)
                }

                loadState.refresh is LoadState.Error -> {
                    val error = loadState.refresh as LoadState.Error
                    initError(error.error)

                    if (photosAdapter?.itemCount == 0) {
                        binding.recyclerView.isVisible = false
                        binding.tvMessage.isVisible = true
                    }
                }
            }
        }

        binding.recyclerView.adapter?.stateRestorationPolicy =
            RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
        binding.recyclerView.adapter = concatAdapter
        binding.recyclerView.setHasFixedSize(true)
        val layoutManager = GridLayoutManager(requireContext(), 2)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if (position == 0 && headerAdapter.itemCount > 0) {
                    2
                } else if (position == (concatAdapter?.itemCount
                        ?: 0) - 1 && footerAdapter.itemCount > 0
                ) {
                    2
                } else {
                    1
                }
            }
        }
        binding.recyclerView.layoutManager = layoutManager
    }

    private fun initError(error: Throwable) {
        when (error) {
            is UnsplashError.NoInternetError -> {
                showNoInternConnection()
            }

            is UnsplashError.ForbiddenError -> {
                error(error.error.errors)
            }

            is UnsplashError.UnauthorizedError -> {
                error(error.error.errors)
            }

            is UnsplashError.NotFoundError -> {
                error(error.error.errors)
            }

            is UnsplashError.BadRequestError -> {
                error(error.error.errors)
            }

            is UnsplashError.ServerError -> {
                MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.server_error))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        photosAdapter?.refresh()
                        dialog.dismiss()
                    }.show()
            }

            is UnsplashError.UnknownError -> {
                MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.unknown_error))
                    .setPositiveButton(
                        getString(
                            R.string.ok
                        )
                    ) { dialog, _ ->
                        photosAdapter?.refresh()
                        dialog.dismiss()
                    }.show()
            }
        }
    }

    private fun openPhotosDetail(url: String) {
        if (connection) {
            requireActivity().supportFragmentManager.beginTransaction()
                .add(
                    R.id.fragment_container,
                    DetailFragment.newInstance(url)
                )
                .addToBackStack(null)
                .commit()
        } else {
            showNoInternConnection()
        }
    }

    private fun showNoInternConnection() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.error))
            .setMessage(getString(R.string.noInternetConnection))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun error(errors: List<String>? = emptyList()) {
        MaterialAlertDialogBuilder(requireContext()).setTitle(getString(R.string.error))
            .setMessage(errors?.joinToString())
            .setPositiveButton(getText(R.string.ok)) { dialog, _ ->
                dialog.dismiss()
            }.show()
    }

    private fun setSwipeRefresh() {
        binding.swipeContainer.apply {
            setOnRefreshListener {
                photosAdapter?.refresh()
                isRefreshing = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}