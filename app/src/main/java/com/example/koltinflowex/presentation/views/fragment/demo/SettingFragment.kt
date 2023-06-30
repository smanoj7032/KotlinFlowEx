package com.example.koltinflowex.presentation.views.fragment.demo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.koltinflowex.R
import com.example.koltinflowex.databinding.SettingFragmentBinding
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.example.koltinflowex.presentation.views.fragment.movielist.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : BaseFragment<SettingFragmentBinding>() {
    private val moviesViewModel: MoviesViewModel by viewModels()

    override fun onCreateView(view: View, saveInstanceState: Bundle?) {}

    override fun executePagingApiCall() {}

    override fun executeApiCall() {}

    override fun getLayoutResource(): Int {
        return R.layout.setting_fragment
    }

}