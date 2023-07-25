package com.example.unsplash.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import androidx.paging.map
import com.example.unsplash.data.mapper.toDomainModel
import com.example.unsplash.data.retrofit.ApiInterface
import com.example.unsplash.domain.model.UnsplashPhoto
import com.example.unsplash.domain.repository.GetPhotosRepository
import javax.inject.Inject

class GetPhotosRepositoryImpl @Inject constructor(
    private val apiInterface: ApiInterface
) : GetPhotosRepository {

    companion object {
        const val PAGE_SIZE = 20
    }

    override fun getPhotos(): LiveData<PagingData<UnsplashPhoto>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = PAGE_SIZE + (PAGE_SIZE * 2),
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UnsplashPagingSource(apiInterface) },
        ).liveData.map { it -> it.map { it.toDomainModel() } }
    }
}