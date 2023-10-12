package com.example.koltinflowex.data.dataImpl

import androidx.paging.PagingData
import com.example.koltinflowex.common.network.api.BaseApi
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.data.model.MovieDetail
import com.example.koltinflowex.data.model.MoviesListResponse
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.domain.repository.BaseRepo
import com.example.koltinflowex.presentation.common.executeApiCall
import com.example.koltinflowex.presentation.common.fetchPagingData
import com.example.koltinflowex.presentation.common.pagination.Pagination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class BaseRepoImpl @Inject constructor(private val baseApi: BaseApi) : BaseRepo {

    override suspend fun getUpcomingMoviesList(): Flow<PagingData<Result>> {
        val pagingSource = Pagination(baseApi, "upcoming")
        return fetchPagingData { pagingSource }
    }

    override suspend fun getTopRatedMoviesList(): Flow<PagingData<Result>> {
        val pagingSource = Pagination(baseApi, "top_rated")
        return fetchPagingData { pagingSource }
    }

    override suspend fun getSearchMovie(
        scope: CoroutineScope,
        search: String
    ): Flow<PagingData<Result>> {
        val pagingSource = Pagination(baseApi, "search", search)
        return fetchPagingData { pagingSource }
    }

    override suspend fun getPopularMoviesDetails(id: Int?): Flow<State<MovieDetail>> {
        return executeApiCall { baseApi.getMovieDetails(id) }
    }

    override suspend fun getPopularMovies(page: Int): Flow<State<MoviesListResponse>> {
        return executeApiCall { baseApi.getPopularMoviesList(page, false) }
    }
}