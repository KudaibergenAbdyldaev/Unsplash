package com.example.unsplash.domain.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.example.unsplash.domain.model.UnsplashPhoto

interface GetPhotosRepository {

    fun getPhotos(): LiveData<PagingData<UnsplashPhoto>>

}