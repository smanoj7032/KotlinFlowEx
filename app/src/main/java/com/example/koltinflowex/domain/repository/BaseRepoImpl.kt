package com.example.koltinflowex.domain.repository

import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.PhotosResponse
import com.example.koltinflowex.domain.network.api.BaseApi
import com.example.koltinflowex.domain.network.helper.State
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class BaseRepoImpl @Inject constructor(private val baseApi: BaseApi) : BaseRepo {
    override suspend fun getComment(id: Int): Flow<State<CommentModel>> {
        return flow {
            val comment = baseApi.getComments(id)
            emit(State.success(comment))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllComment(): Flow<State<List<CommentModel>>> {
        return flow {
            val comment = baseApi.getAllComments()
            emit(State.success(comment))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getAllPhotos(): Flow<State<List<PhotosResponse>>> {
        return flow {
            val photosResponse = baseApi.getAllPhotos()
            emit(State.success(photosResponse))
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun getMeme(): Flow<State<List<MemeResponse>>> {
        return flow {
            val memeResponse = baseApi.getMeme(
                "programming-memes-images.p.rapidapi.com",
                "1597887038msh5fd46b97e924c27p1c29edjsn005a849a17a0"
            )
            emit(State.success(memeResponse))
        }.flowOn(Dispatchers.IO)
    }
}