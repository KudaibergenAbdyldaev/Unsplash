package com.example.unsplash.domain.usecase

import com.example.unsplash.domain.repository.GetPhotosRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(
    private val repository: GetPhotosRepository
) {

    fun getPhotos() = repository.getPhotos()

}