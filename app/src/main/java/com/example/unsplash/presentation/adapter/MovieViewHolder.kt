package com.example.unsplash.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.unsplash.databinding.PopularMovieItemBinding
import com.example.unsplash.domain.model.UnsplashPhoto
import com.example.unsplash.presentation.extentions.loadImageFromUrl


class MovieViewHolder(private val binding: PopularMovieItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(item: UnsplashPhoto?) {
        binding.image.loadImageFromUrl(item?.urls?.regular?:"")
    }
}