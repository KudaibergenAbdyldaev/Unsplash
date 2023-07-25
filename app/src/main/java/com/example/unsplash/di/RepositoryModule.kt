package com.example.unsplash.di

import com.example.unsplash.data.repository.GetPhotosRepositoryImpl
import com.example.unsplash.domain.repository.GetPhotosRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object RepositoryModule {

    @Provides
    fun provideGetPhotosRepository(repository: GetPhotosRepositoryImpl): GetPhotosRepository {
        return repository
    }

}