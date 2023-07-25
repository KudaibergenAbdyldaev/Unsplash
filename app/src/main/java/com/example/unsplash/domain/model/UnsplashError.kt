package com.example.unsplash.domain.model

sealed class UnsplashError : Exception() {
    object NoInternetError : UnsplashError()
    object ServerError : UnsplashError()
    data class BadRequestError(val error: Errors) : UnsplashError()
    data class UnauthorizedError(val error: Errors) : UnsplashError()
    data class ForbiddenError(val error: Errors) : UnsplashError()
    data class NotFoundError(val error: Errors) : UnsplashError()
    object UnknownError : UnsplashError()
}