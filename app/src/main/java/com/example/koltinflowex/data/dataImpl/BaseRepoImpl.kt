package com.example.koltinflowex.data.dataImpl

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.koltinflowex.common.network.api.BaseApi
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.MovieDetail
import com.example.koltinflowex.data.model.PhotosResponse
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
    override suspend fun getComment(id: Int?): Flow<State<CommentModel>> {
        return executeApiCall { baseApi.getComments(id) }
    }

    override suspend fun getAllComment(): Flow<State<List<CommentModel>>> {
        return executeApiCall { baseApi.getAllComments() }
    }

    override suspend fun getAllPhotos(): Flow<State<List<PhotosResponse>>> {
        return executeApiCall { baseApi.getAllPhotos() }
    }

    override suspend fun getMeme(): Flow<State<List<MemeResponse>>> {
        return executeApiCall {
            baseApi.getMeme(
                "programming-memes-images.p.rapidapi.com",
                "1597887038msh5fd46b97e924c27p1c29edjsn005a849a17a0"
            )
        }
    }


    override suspend fun getUpcomingMoviesList(scope: CoroutineScope): Flow<State<PagingData<Result>>> {
        return Pager(config = PagingConfig(pageSize = 10),
            pagingSourceFactory = { Pagination(baseApi, "upcoming") }).flow.cachedIn(scope)
            .map { pagindata ->
                State.success(pagindata)
            }.catch { e -> emit(State.error(e.localizedMessage, true)) }
            .onStart { emit(State.loading()) }
    }

    override suspend fun getTopRatedMoviesList(scope: CoroutineScope): Flow<State<PagingData<Result>>> {
        return Pager(
            PagingConfig(10),
            pagingSourceFactory = {
                Pagination(
                    baseApi,
                    "top_rated"
                )
            }).flow.cachedIn(scope).map { State.success(it) }.catch {
            emit(State.error(it.message, true))
        }.onStart { emit(State.loading()) }
    }

    override suspend fun getPopularMoviesDetails(id: Int?): Flow<State<MovieDetail>> {
        return executeApiCall { baseApi.getMovieDetails(id) }
    }

    override suspend fun getMoviesList(scope: CoroutineScope): Flow<State<PagingData<Result>>> {
        return Pager(config = PagingConfig(pageSize = 10), pagingSourceFactory = {
            Pagination(baseApi, "popular")
        }).flow.cachedIn(scope).map { pagingData ->
            State.success(pagingData)
        }.catch { e ->
            emit(State.error(e.localizedMessage, true))
        }.onStart {
            emit(State.loading())
        }
    }


}