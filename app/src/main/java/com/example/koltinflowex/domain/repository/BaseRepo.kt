package com.example.koltinflowex.domain.repository

import androidx.paging.PagingData
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.data.model.MovieDetail
import com.example.koltinflowex.data.model.MoviesListResponse
import com.example.koltinflowex.data.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface BaseRepo {

    suspend fun getPopularMoviesDetails(id: Int?): Flow<State<MovieDetail>>
    suspend fun getPopularMovies(scope: CoroutineScope): Flow<State<PagingData<Result>>>
    suspend fun getUpcomingMoviesList(scope: CoroutineScope): Flow<State<PagingData<Result>>>
    suspend fun getTopRatedMoviesList(scope: CoroutineScope): Flow<State<MoviesListResponse>>
    suspend fun getSearchMovie(
        scope: CoroutineScope, search: String,
        ): Flow<State<PagingData<Result>>>
}