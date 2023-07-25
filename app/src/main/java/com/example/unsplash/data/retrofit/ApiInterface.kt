package com.example.unsplash.data.retrofit

import com.example.unsplash.data.model.UnsplashPhotoDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ApiInterface {

    @Headers("Authorization: Client-ID b0UeZo9kpS1qJzYwal_McQTVzF96VUEG-zyAuxeAL3I")
    @GET("photos/")
    suspend fun getPhotos(
        @Query("page") page: Int
    ): Response<List<UnsplashPhotoDto>>


}