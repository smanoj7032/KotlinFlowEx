package com.example.koltinflowex.common.network.api

import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.MovieDetail
import com.example.koltinflowex.data.model.MoviesListResponse
import com.example.koltinflowex.data.model.PhotosResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface BaseApi {
    @GET("/comments/{id}")
    suspend fun getComments(@Path("id") id: Int?): CommentModel

    @GET("comments")
    suspend fun getAllComments(): List<CommentModel>

    @GET("photos")
    suspend fun getAllPhotos(): List<PhotosResponse>

    @GET("https://programming-memes-images.p.rapidapi.com/v1/memes")
    suspend fun getMeme(
        @Header("X-RapidAPI-Host") token: String,
        @Header("X-RapidAPI-Key") apiKey: String,
    ): List<MemeResponse>

    @GET("movie/popular")
    suspend fun getPopularMoviesList(@Query("page") page: Int): MoviesListResponse
    @GET("movie/top_rated")
    suspend fun getTopRatedMoviesList(@Query("page") page: Int): MoviesListResponse

    @GET("movie/upcoming")
    suspend fun getTvSerialMoviesList(@Query("page") page: Int): MoviesListResponse

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(@Path("movie_id") id: Int?): MovieDetail
}