package com.example.koltinflowex.domain.repository

import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.PhotosResponse
import com.example.koltinflowex.domain.network.helper.State
import kotlinx.coroutines.flow.Flow

interface BaseRepo {
    suspend fun getComment(id: Int): Flow<State<CommentModel>>
    suspend fun getAllComment(): Flow<State<List<CommentModel>>>
    suspend fun getAllPhotos(): Flow<State<List<PhotosResponse>>>
    suspend fun getMeme(): Flow<State<List<MemeResponse>>>
}