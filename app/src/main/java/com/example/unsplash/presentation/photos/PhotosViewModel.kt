package com.example.unsplash.presentation.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.unsplash.domain.usecase.GetPhotosUseCase
import com.example.unsplash.presentation.ConnectionLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    getPhotosUseCase: GetPhotosUseCase,
    _connectionLiveData: ConnectionLiveData
) : ViewModel() {

    val photos = getPhotosUseCase.getPhotos().cachedIn(viewModelScope)

    val connectionLiveData: LiveData<Boolean> = _connectionLiveData

}