package com.example.koltinflowex.domain.repository

import androidx.paging.PagingData
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.MovieDetail
import com.example.koltinflowex.data.model.MoviesListResponse
import com.example.koltinflowex.data.model.PhotosResponse
import com.example.koltinflowex.data.model.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

interface BaseRepo {
    suspend fun getComment(id: Int?): Flow<State<CommentModel>>
    suspend fun getAllComment(): Flow<State<List<CommentModel>>>
    suspend fun getAllPhotos(): Flow<State<List<PhotosResponse>>>
    suspend fun getMeme(): Flow<State<List<MemeResponse>>>
    suspend fun getPopularMoviesDetails(id: Int?): Flow<State<MovieDetail>>
    suspend fun getMoviesList(scope: CoroutineScope): Flow<State<PagingData<Result>>>
    suspend fun getUpcomingMoviesList(scope: CoroutineScope): Flow<State<PagingData<Result>>>
    suspend fun getTopRatedMoviesList(scope: CoroutineScope): Flow<State<PagingData<Result>>>
}