package com.example.koltinflowex.common.network.api

import com.example.koltinflowex.data.model.MovieDetail
import com.example.koltinflowex.data.model.MoviesListResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BaseApi {
    @GET("movie/popular")
    suspend fun getPopularMoviesList(@Query("page") page: Int): MoviesListResponse

    @GET("search/movie")
    suspend fun searchMovie(
        @Query("language") language: String,
        @Query("query") search: String?,
        @Query("page") page: Int,
        @Query("include_adult") include_adult: Boolean,
    ): MoviesListResponse

    @GET("movie/top_rated")
    suspend fun getTopRatedMoviesList(@Query("page") page: Int): MoviesListResponse

    @GET("movie/upcoming")
    suspend fun getTvSerialMoviesList(@Query("page") page: Int): MoviesListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") id: Int?): MovieDetail
}