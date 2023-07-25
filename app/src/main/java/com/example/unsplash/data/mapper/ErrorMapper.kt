package com.example.unsplash.data.mapper

import com.example.unsplash.data.model.ErrorDto
import com.example.unsplash.domain.model.Errors

fun ErrorDto.toDomainModel(): Errors {
    return Errors(
        this.errors
    )
}

fun Errors.toDto(): ErrorDto {
    return ErrorDto(
        this.errors
    )
}
