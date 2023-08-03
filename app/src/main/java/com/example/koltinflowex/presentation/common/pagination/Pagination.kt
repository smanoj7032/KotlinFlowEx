package com.example.koltinflowex.presentation.common.pagination

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.koltinflowex.common.network.api.BaseApi
import com.example.koltinflowex.data.model.MoviesListResponse
import com.example.koltinflowex.data.model.Result
import retrofit2.Response
import java.io.IOException

class Pagination(
    private val movieApiService: BaseApi,
    private val type: String,
    private val search: String? = null,
) : PagingSource<Int, Result>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {
            val page = params.key ?: 1
            val response = when (type) {
                "search" -> movieApiService.searchMovie("en-US", search, page, false)
                "top_rated" -> movieApiService.getTopRatedMoviesList(page)
                "upcoming" -> movieApiService.getTvSerialMoviesList(page)
                else -> movieApiService.getPopularMoviesList(page)
            }
            if (response.isSuccessful) {
                val movies = response.body()?.results ?: emptyList()
                LoadResult.Page(
                    data = movies,
                    prevKey = if (page > 1) page - 1 else null,
                    nextKey = if (movies.isNotEmpty()) page + 1 else null
                )
            } else {
                val errorBody = response.errorBody()
                val errorMessage = errorBody?.string() ?: response.message()
                Log.e("ERROR-->>", "load: $errorMessage")
                LoadResult.Error(IOException("Error loading data: ${response.code()} $errorMessage"))

            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        } catch (io: IOException) {
            LoadResult.Error(io)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}


