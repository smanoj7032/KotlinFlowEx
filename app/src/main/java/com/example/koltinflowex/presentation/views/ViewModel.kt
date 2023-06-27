package com.example.koltinflowex.presentation.views

import androidx.paging.PagingData
import com.example.koltinflowex.common.network.helper.State
import com.example.koltinflowex.common.network.helper.Status
import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.MoviesListResponse
import com.example.koltinflowex.data.model.PhotosResponse
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.domain.repository.BaseRepo
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import com.example.koltinflowex.presentation.common.emitter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(
    private val baseRepo: BaseRepo
) : BaseViewModel() {
    val searchCommentStateFlow = MutableStateFlow(State(Status.LOADING, CommentModel(), "", false))
    val allCommentsFlow = MutableStateFlow(State(Status.LOADING, listOf<CommentModel>(), "", false))
    val allPhotos = MutableStateFlow(State(Status.LOADING, listOf<PhotosResponse>(), "", false))
    val allMeme = MutableStateFlow(State(Status.LOADING, listOf<MemeResponse>(), "", false))
    val moviesListResponse =
        MutableStateFlow(State(Status.LOADING, MoviesListResponse(), "", false))
    val movieList = MutableStateFlow<State<PagingData<Result>>>(State.loading())


    fun getNewComment(id: Int?) {
        launchTask { baseRepo.getComment(id).emitter(searchCommentStateFlow, false) }
    }

    fun getAllComment() {
        launchTask { baseRepo.getAllComment().emitter(allCommentsFlow, false) }
    }

    private fun getAllPhotos() {
        launchTask { baseRepo.getAllPhotos().emitter(allPhotos, false) }
    }

    private fun getMeme() {
        launchTask { baseRepo.getMeme().emitter(allMeme, false) }
    }

    fun getMoviesList() {
        launchTask {
            baseRepo.getMoviesList().emitter(movieList, false)
        }
    }

}