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
    suspend fun getPopularMovies(page:Int): Flow<State<MoviesListResponse>>
    suspend fun getUpcomingMoviesList(): Flow<PagingData<Result>>
    suspend fun getTopRatedMoviesList(): Flow<PagingData<Result>>
    suspend fun getSearchMovie(
        scope: CoroutineScope, search: String,
    ): Flow<PagingData<Result>>
}