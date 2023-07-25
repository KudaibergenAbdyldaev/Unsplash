package com.example.unsplash.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplash.data.model.UnsplashPhotoDto
import com.example.unsplash.data.retrofit.ApiInterface

class UnsplashPagingSource(
    private val apiInterface: ApiInterface,
) : PagingSource<Int, UnsplashPhotoDto>() {
    companion object {
        private const val STARTING_PAGE_INDEX = 1
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, UnsplashPhotoDto> {
        return try {
            val page = params.key ?: STARTING_PAGE_INDEX
            val response = apiInterface.getPhotos(page)
            val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
            val nextKey = if (response.isEmpty()) null else page + 1

            LoadResult.Page(
                data = response,
                prevKey = prevKey,
                nextKey = nextKey
            )
        } catch (e: Exception) {
            Log.e("Exception", e.message.toString())
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, UnsplashPhotoDto>): Int? {
        if (state.anchorPosition == null) {
            return null
        }
        val anchorPage = state.closestPageToPosition(state.anchorPosition ?: 0)
        return anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
    }
}