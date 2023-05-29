package com.example.koltinflowex.domain.network.api

import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.PhotosResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BaseApi {
    @GET("/comments/{id}")
    suspend fun getComments(@Path("id") id: Int): CommentModel

    @GET("comments")
    suspend fun getAllComments(): List<CommentModel>

    @GET("photos")
    suspend fun getAllPhotos(): List<PhotosResponse>

    @GET("https://programming-memes-images.p.rapidapi.com/v1/memes")
    suspend fun getMeme(
        @Header("X-RapidAPI-Host") token: String,
        @Header("X-RapidAPI-Key") apiKey: String,
    ): List<MemeResponse>
}