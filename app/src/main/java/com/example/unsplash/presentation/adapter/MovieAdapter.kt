package com.example.unsplash.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.unsplash.R
import com.example.unsplash.databinding.PopularMovieItemBinding
import com.example.unsplash.domain.model.UnsplashPhoto

class MovieAdapter : PagingDataAdapter<UnsplashPhoto, MovieViewHolder>(DiffUtilCallBack) {

    var onMovieItemClickListener: ((String) -> Unit)? = null

    override fun onBindViewHolder(holderMovie: MovieViewHolder, position: Int) {
        holderMovie.bind(getItem(position))
        holderMovie.itemView.setOnClickListener {
            getItem(position)?.id?.let {
                onMovieItemClickListener?.invoke(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding: PopularMovieItemBinding = PopularMovieItemBinding.bind(
            LayoutInflater.from(parent.context).inflate(
                R.layout.popular_movie_item, parent, false
            )
        )
        return MovieViewHolder(binding)
    }

}