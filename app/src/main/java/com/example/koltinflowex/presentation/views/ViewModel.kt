package com.example.koltinflowex.presentation.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.PhotosResponse
import com.example.koltinflowex.domain.network.helper.State
import com.example.koltinflowex.domain.network.helper.Status
import com.example.koltinflowex.domain.repository.BaseRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewModel @Inject constructor(private val baseRepo: BaseRepo) : ViewModel() {
    val searchCommentStateFlow = MutableStateFlow(State(Status.SUCCESS, CommentModel(), ""))
    val allCommentsFlow = MutableStateFlow(State(Status.SUCCESS, listOf<CommentModel>(), ""))
    val allPhotos = MutableStateFlow(State(Status.SUCCESS, listOf<PhotosResponse>(), ""))
    val allMeme = MutableStateFlow(State(Status.SUCCESS, listOf<MemeResponse>(), ""))

    fun getNewComment(id: Int?) {
        searchCommentStateFlow.value = State.loading()
        viewModelScope.launch {
            if (id != null) {
                baseRepo.getComment(id).catch {
                    searchCommentStateFlow.value = State.error(it.message)
                }.collect {
                    searchCommentStateFlow.value = State.success(it.data)
                }
            }
        }
    }

     fun getAllComment() {
        allCommentsFlow.value = State.loading()
        viewModelScope.launch {
            baseRepo.getAllComment().catch {
                allCommentsFlow.value = State.error(it.message)
            }.collect {
                allCommentsFlow.value = State.success(it.data)
            }
        }
    }

    private fun getAllPhotos() {
        allPhotos.value = State.loading()
        viewModelScope.launch {
            baseRepo.getAllPhotos().catch { allPhotos.value = State.error(it.message) }.collect {
                allPhotos.value = State.success(it.data)
            }
        }
    }

     fun getMeme() {
        allMeme.value = State.loading()
        viewModelScope.launch {
            baseRepo.getMeme().catch { allMeme.value = State.error(it.message) }.collect {
                allMeme.value = State.success(it.data)
            }
        }
    }

    init {
        getAllComment()
        // getAllPhotos()
       // getMeme()
    }
}