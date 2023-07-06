package com.example.koltinflowex.data.dataImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.koltinflowex.common.network.api.BaseApi
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.data.model.MovieDetail
import com.example.koltinflowex.data.model.MoviesListResponse
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.domain.repository.BaseRepo
import com.example.koltinflowex.presentation.common.executeApiCall
import com.example.koltinflowex.presentation.common.pagination.Pagination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class BaseRepoImpl @Inject constructor(private val baseApi: BaseApi) : BaseRepo {

    override suspend fun getUpcomingMoviesList(scope: CoroutineScope): Flow<State<PagingData<Result>>> {
        return Pager(config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { Pagination(baseApi, "upcoming") }).flow.cachedIn(scope)
            .map { pagindata ->
                State.success(pagindata)
            }.catch { e -> emit(State.error(e.localizedMessage, true)) }
            .onStart { emit(State.loading()) }
    }

    override suspend fun getTopRatedMoviesList(scope: CoroutineScope): Flow<State<MoviesListResponse>> {
        return executeApiCall { baseApi.getTopRatedMoviesList(1) }
    }

    override suspend fun getSearchMovie(
        scope: CoroutineScope, search: String
    ): Flow<State<PagingData<Result>>> {
        return Pager(
            PagingConfig(10, enablePlaceholders = true),
            pagingSourceFactory = {
                Pagination(
                    baseApi,
                    "search",
                    search
                )
            }).flow.cachedIn(scope)
            .map { State.success(it) }.catch {
                emit(State.error(it.message, true))
            }.onStart { emit(State.loading()) }
    }

    override suspend fun getPopularMoviesDetails(id: Int?): Flow<State<MovieDetail>> {
        return executeApiCall { baseApi.getMovieDetails(id) }
    }

    override suspend fun getPopularMovies(scope: CoroutineScope): Flow<State<PagingData<Result>>> {
        return Pager(
            config = PagingConfig(pageSize = 10, enablePlaceholders = false),
            pagingSourceFactory = {
                Pagination(baseApi, "popular")
            }).flow.cachedIn(scope).map { pagingData ->
            if (Pagination(baseApi, "popular").errorMessage != null)
                State.error(Pagination(baseApi, "popular").errorMessage, true)
            else State.success(pagingData)

        }.catch { e ->
            emit(State.error(e.message, true))
        }.onStart {
            emit(State.loading())
        }
    }
}