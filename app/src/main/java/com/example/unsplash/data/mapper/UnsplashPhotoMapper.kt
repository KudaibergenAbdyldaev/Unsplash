package com.example.unsplash.data.mapper

import com.example.unsplash.data.model.UnsplashPhotoDto
import com.example.unsplash.data.model.UnsplashPhotoUrlsDto
import com.example.unsplash.domain.model.UnsplashPhoto
import com.example.unsplash.domain.model.UnsplashPhotoUrls

fun UnsplashPhotoDto.toDomainModel(): UnsplashPhoto {
    return UnsplashPhoto(
        id = this.id,
        urls = this.urls.toDomainModel()
    )
}

fun UnsplashPhotoUrlsDto.toDomainModel(): UnsplashPhotoUrls {
    return UnsplashPhotoUrls(
        full = this.full,
        regular = this.regular,
        small = this.small
    )
}

fun UnsplashPhoto.toDto(): UnsplashPhotoDto {
    return UnsplashPhotoDto(
        id = this.id,
        urls = this.urls.toDto()
    )
}

fun UnsplashPhotoUrls.toDto(): UnsplashPhotoUrlsDto {
    return UnsplashPhotoUrlsDto(
        full = this.full,
        regular = this.regular,
        small = this.small
    )
}