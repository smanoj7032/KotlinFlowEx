package com.example.koltinflowex.presentation.views.fragment.demo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.example.koltinflowex.R
import com.example.koltinflowex.databinding.TvSerialFragmentBinding
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.example.koltinflowex.presentation.views.fragment.movielist.ViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TvSerialFragment : BaseFragment<TvSerialFragmentBinding>() {
    private val viewModel: ViewModel by viewModels()

    override fun onCreateView(view: View, saveInstanceState: Bundle?) {}

    override fun executePagingApiCall() {viewModel.getUpcomingMoviesList()}

    override fun executeApiCall() {}

    override fun getLayoutResource(): Int {
        return R.layout.tv_serial_fragment
    }
}