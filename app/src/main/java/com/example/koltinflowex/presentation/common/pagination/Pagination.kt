package com.example.koltinflowex.presentation.common.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.koltinflowex.common.network.api.BaseApi
import com.example.koltinflowex.data.model.Result

class Pagination(
    private val movieApiService: BaseApi,
    private val type: String,
    private val search: String? = null,
) : PagingSource<Int, Result>() {
    var errorMessage: String? = ""
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val page = params.key ?: 1
            val response = when (type) {
                "popular" -> {
                    movieApiService.getPopularMoviesList(page)
                }

                "top_rated" -> {
                    movieApiService.getTopRatedMoviesList(page)
                }

                "upcoming" -> {
                    movieApiService.getTvSerialMoviesList(page)
                }

                else -> {
                    movieApiService.searchMovie("en-US", search, page, true)
                }
            }
            if (response.isSuccessful) {
                errorMessage = null
                val movies = response.body()?.results ?: emptyList()
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page > 1) page - 1 else null,
                    nextKey = if (movies.isNotEmpty()) page + 1 else null
                )
            } else {
                val errorBody = response.errorBody()
                val errorMessage = errorBody?.string() ?: response.message()
                Log.e("ERROR-->>", "load: ${errorMessage}", )
                LoadResult.Error(Throwable(errorMessage))
            }

        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}


