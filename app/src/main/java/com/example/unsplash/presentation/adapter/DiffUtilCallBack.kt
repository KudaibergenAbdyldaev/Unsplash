package com.example.unsplash.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.unsplash.domain.model.UnsplashPhoto

object DiffUtilCallBack : DiffUtil.ItemCallback<UnsplashPhoto>() {
    override fun areItemsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UnsplashPhoto, newItem: UnsplashPhoto): Boolean {
        return oldItem == newItem
    }
}