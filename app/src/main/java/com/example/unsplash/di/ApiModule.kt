package com.example.unsplash.di

import com.example.unsplash.data.check_internet_connection.ConnectivityInterceptor
import com.example.unsplash.data.retrofit.ApiFactory
import com.example.unsplash.data.retrofit.ApiInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object ApiModule {

    @Provides
    fun provideApiFactory(connectivityInterceptor: ConnectivityInterceptor): ApiFactory {
        return ApiFactory(connectivityInterceptor)
    }

    @Provides
    fun provideApiInterface(apiFactory: ApiFactory): ApiInterface {
        return apiFactory.apiService
    }
}