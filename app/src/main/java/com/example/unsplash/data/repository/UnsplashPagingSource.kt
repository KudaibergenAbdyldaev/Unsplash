package com.example.unsplash.data.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.unsplash.data.mapper.toDomainModel
import com.example.unsplash.data.model.ErrorDto
import com.example.unsplash.data.model.UnsplashPhotoDto
import com.example.unsplash.data.retrofit.ApiInterface
import com.example.unsplash.domain.model.UnsplashError
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.IOException

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

            if (response.isSuccessful) {
                val data = response.body() ?: emptyList()
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (data.isEmpty()) null else page + 1

                LoadResult.Page(
                    data = data,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } else {
                val errorDto = response.errorBody()?.let { errorBody ->
                    runCatching {
                        val errorJson = errorBody.string()
                        Json.decodeFromString<ErrorDto>(errorJson)
                    }.getOrNull()
                }
                when (response.code()) {
                    400 -> errorDto?.let { LoadResult.Error(UnsplashError.BadRequestError(it.toDomainModel())) }
                        ?: LoadResult.Error(UnsplashError.UnknownError)

                    401 -> errorDto?.let { LoadResult.Error(UnsplashError.UnauthorizedError(it.toDomainModel())) }
                        ?: LoadResult.Error(UnsplashError.UnknownError)

                    403 -> errorDto?.let { LoadResult.Error(UnsplashError.ForbiddenError(it.toDomainModel())) }
                        ?: LoadResult.Error(UnsplashError.UnknownError)

                    404 -> errorDto?.let { LoadResult.Error(UnsplashError.NotFoundError(it.toDomainModel())) }
                        ?: LoadResult.Error(UnsplashError.UnknownError)

                    500 -> LoadResult.Error(UnsplashError.ServerError)
                    else -> LoadResult.Error(UnsplashError.UnknownError)
                }
            }
        } catch (e: IOException) {
            LoadResult.Error(UnsplashError.NoInternetError)
        } catch (e: Exception) {
            LoadResult.Error(UnsplashError.UnknownError)
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