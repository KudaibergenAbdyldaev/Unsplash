package com.example.unsplash.presentation.photos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.unsplash.presentation.adapter.PhotosAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PhotosFragment : Fragment() {

    private var _binding: FragmentPhotosBinding? = null
    private val binding: FragmentPhotosBinding
        get() = _binding ?: throw RuntimeException("FragmentPhotosBinding is null")

    private val photosAdapter: PhotosAdapter by lazy {
        PhotosAdapter()
    }

    private var connection = true

    private val viewModel: PhotosViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
        setRecyclerViewData()
//        openMovieDetail()
//        checkNetworkConnection()
    }

    private fun checkNetworkConnection() {
        viewModel.connectionLiveData.observe(viewLifecycleOwner) {
            connection = it
            if (it)
                photosAdapter.refresh()
            else
                showNoInternConnection()
        }
    }


    private fun setRecyclerViewData() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.photos.collectLatest {
                    photosAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData = it)
                }
            }
        }
    }

    private fun setUpRecyclerView() {
        val headerAdapter = LoaderStateAdapter()
        val footerAdapter = LoaderStateAdapter()

        val concatAdapter = photosAdapter.withLoadStateHeaderAndFooter(
            header = headerAdapter,
            footer = footerAdapter
        )
        photosAdapter.addLoadStateListener { loadState ->
            when {
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
                } else if (position == concatAdapter.itemCount - 1 && footerAdapter.itemCount > 0) {
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
                Log.e("UnsplashError", "NoInternetError")
            }

            is UnsplashError.ForbiddenError -> {
                Log.e("UnsplashError", "ForbiddenError")
            }

            is UnsplashError.UnauthorizedError -> {
                Log.e("UnsplashError", error.error.errors.joinToString())
            }

            is UnsplashError.NotFoundError -> {
                error.error.errors
                Log.e("UnsplashError", "NotFoundError")
            }

            is UnsplashError.BadRequestError -> {
                Log.e("UnsplashError", "BadRequestError")
            }

            is UnsplashError.ServerError -> {
                Log.e("UnsplashError", "ServerError")
            }

            is UnsplashError.UnknownError -> {
                Log.e("UnsplashError", "UnknownError")
            }
        }
    }

    private fun openMovieDetail() {
        photosAdapter.onPhotosItemClickListener = {
            if (connection) {
//                requireActivity().supportFragmentManager.beginTransaction()
//                    .replace(
//                        R.id.fragment_container,
//                        DetailMovieFragment.newInstanceDetailMovieFragment(it)
//                    )
//                    .addToBackStack(null)
//                    .commit()
            } else {
                showNoInternConnection()
            }
        }
    }

    private fun showNoInternConnection() {
        Toast.makeText(
            requireContext(),
            getString(R.string.check_internet),
            Toast.LENGTH_SHORT
        ).show()
    }

}