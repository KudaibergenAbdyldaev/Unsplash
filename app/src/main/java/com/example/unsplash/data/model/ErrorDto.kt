package com.example.unsplash.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ErrorDto(
    val errors: List<String>
)