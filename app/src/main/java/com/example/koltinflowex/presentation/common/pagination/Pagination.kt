package com.example.koltinflowex.presentation.common.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.koltinflowex.common.network.api.BaseApi
import com.example.koltinflowex.data.model.Result

class Pagination(private val movieService: BaseApi) :
    PagingSource<Int, Result>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        return try {

            val page = params.key ?: 1
            val response = movieService.getPopularMoviesList(page)

            LoadResult.Page(
                data = response.results ?: emptyList(),
                prevKey = if (page > 1) page - 1 else null,
                nextKey = if (page < (response.totalPages ?: 0)) page + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return null
    }
}
