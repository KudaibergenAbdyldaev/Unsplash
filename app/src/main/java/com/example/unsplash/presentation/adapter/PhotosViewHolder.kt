package com.example.unsplash.presentation.adapter

import androidx.recyclerview.widget.RecyclerView
import com.example.unsplash.databinding.PhotosItemBinding
import com.example.unsplash.domain.model.UnsplashPhoto
import com.example.unsplash.presentation.extentions.loadImageFromUrl


class PhotosViewHolder(private val binding: PhotosItemBinding) :
    RecyclerView.ViewHolder(binding.root) {

    var onPhotosItemClickListener: ((String) -> Unit)? = null

    fun bind(item: UnsplashPhoto?) {
        binding.image.loadImageFromUrl(item?.urls?.regular ?: "")
        binding.image.setOnClickListener {
            onPhotosItemClickListener?.invoke(item?.urls?.full ?: "")
        }
    }
}