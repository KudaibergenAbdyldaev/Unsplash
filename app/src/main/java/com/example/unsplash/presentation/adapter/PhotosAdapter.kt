package com.example.unsplash.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.unsplash.R
import com.example.unsplash.databinding.PhotosItemBinding
import com.example.unsplash.domain.model.UnsplashPhoto

class PhotosAdapter : PagingDataAdapter<UnsplashPhoto, PhotosViewHolder>(DiffUtilCallBack) {

    var onPhotosItemClickListener: ((String) -> Unit)? = null

    override fun onBindViewHolder(holderPhotos: PhotosViewHolder, position: Int) {
        holderPhotos.bind(getItem(position))
        holderPhotos.itemView.setOnClickListener {
            getItem(position)?.id?.let {
                onPhotosItemClickListener?.invoke(it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotosViewHolder {
        val binding: PhotosItemBinding = PhotosItemBinding.bind(
            LayoutInflater.from(parent.context).inflate(
                R.layout.photos_item, parent, false
            )
        )
        return PhotosViewHolder(binding)
    }

}