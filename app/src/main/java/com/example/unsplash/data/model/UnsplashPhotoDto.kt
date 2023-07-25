package com.example.unsplash.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashPhotoDto(
    val id: String,
    val urls: UnsplashPhotoUrlsDto,
)