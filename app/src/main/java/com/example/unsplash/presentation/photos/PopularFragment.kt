package com.example.unsplash.presentation.photos

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.unsplash.R
import com.example.unsplash.databinding.FragmentPopularBinding
import com.example.unsplash.presentation.adapter.LoaderStateAdapter
import com.example.unsplash.presentation.adapter.MovieAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : Fragment() {

    private var _binding: FragmentPopularBinding? = null
    private val binding: FragmentPopularBinding
        get() = _binding ?: throw RuntimeException("FragmentPopularBinding is null")

    private val movieAdapter: MovieAdapter by lazy {
        MovieAdapter()
    }

    private var connection = true

    private val viewModel: PopularViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPopularBinding.inflate(inflater, container, false)
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
                movieAdapter.refresh()
            else
                showNoInternConnection()
        }
    }


    private fun setRecyclerViewData() {
        viewModel.popularMovie.observe(viewLifecycleOwner) {
            movieAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData = it)
        }

    }

    private fun setUpRecyclerView() {
        val headerAdapter = LoaderStateAdapter()
        val footerAdapter = LoaderStateAdapter()

        val concatAdapter = movieAdapter.withLoadStateHeaderAndFooter(
            header = headerAdapter,
            footer = footerAdapter
        )
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

    private fun openMovieDetail() {
        movieAdapter.onMovieItemClickListener = {
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