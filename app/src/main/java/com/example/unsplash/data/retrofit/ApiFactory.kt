package com.example.unsplash.data.retrofit

import com.example.unsplash.data.check_internet_connection.ConnectivityInterceptor
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ApiFactory @Inject constructor(connectivityInterceptor: ConnectivityInterceptor) {

    companion object {
        private const val BASE_URL = "https://api.unsplash.com/"
    }


    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(LoggingInterceptor().provideLoggingInterceptor())
        .addInterceptor(
            connectivityInterceptor
        )
        .connectTimeout(90, TimeUnit.SECONDS)
        .readTimeout(90, TimeUnit.SECONDS)
        .writeTimeout(90, TimeUnit.SECONDS)
        .build()

    private val contentType = "application/json".toMediaType()
    private val json = Json {
        ignoreUnknownKeys = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(json.asConverterFactory(contentType))
        .build()

    val apiService = retrofit.create(ApiInterface::class.java)
}