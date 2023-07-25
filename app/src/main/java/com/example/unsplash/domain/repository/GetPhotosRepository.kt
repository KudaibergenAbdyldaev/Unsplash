package com.example.unsplash.domain.repository

import androidx.paging.PagingData
import com.example.unsplash.domain.model.UnsplashPhoto
import kotlinx.coroutines.flow.Flow

interface GetPhotosRepository {

    fun getPhotos(): Flow<PagingData<UnsplashPhoto>>

}