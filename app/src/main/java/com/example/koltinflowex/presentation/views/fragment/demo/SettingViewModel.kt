package com.example.koltinflowex.presentation.views.fragment.demo

import com.example.koltinflowex.domain.repository.BaseRepo
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val baseRepo: BaseRepo) : BaseViewModel() {

}