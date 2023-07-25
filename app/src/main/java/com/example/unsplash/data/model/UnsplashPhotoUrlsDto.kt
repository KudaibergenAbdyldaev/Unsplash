package com.example.unsplash.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UnsplashPhotoUrlsDto(
    val full: String?,
    val regular: String?,
    val small: String?,
)