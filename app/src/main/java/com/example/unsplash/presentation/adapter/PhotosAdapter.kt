package com.example.unsplash.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import com.example.unsplash.R
import com.example.unsplash.databinding.PhotosItemBinding
import com.example.unsplash.domain.model.UnsplashPhoto

class PhotosAdapter(private val onClickListener: OnClickListener) : PagingDataAdapter<UnsplashPhoto, PhotosViewHolder>(DiffUtilCallBack) {


    override fun onBindViewHolder(holderPhotos: PhotosViewHolder, position: Int) {
        holderPhotos.bind(getItem(position))
        holderPhotos.itemView.setOnClickListener {
            getItem(position)?.urls?.full?.let {
                onClickListener.onClick(it)
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