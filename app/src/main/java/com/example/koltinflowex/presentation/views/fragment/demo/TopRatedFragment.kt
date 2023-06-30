package com.example.koltinflowex.presentation.views.fragment.demo

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.koltinflowex.BR
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.databinding.ItemPhotosListBinding
import com.example.koltinflowex.databinding.TopRatedFragmentBinding
import com.example.koltinflowex.presentation.common.adapter.LoadMoreAdapter
import com.example.koltinflowex.presentation.common.adapter.RVAdapterWithPaging
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.example.koltinflowex.presentation.common.base.DoubleClickListener
import com.example.koltinflowex.presentation.common.customCollector
import com.example.koltinflowex.presentation.common.loadImage
import com.example.koltinflowex.presentation.views.fragment.movielist.ViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TopRatedFragment : BaseFragment<TopRatedFragmentBinding>() {
    private val viewModel: ViewModel by viewModels()
    private lateinit var topRatedMoviesAdapter: RVAdapterWithPaging<Result, ItemPhotosListBinding>

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateView(view: View, saveInstanceState: Bundle?) {
        setAdapter()
        setObserver()
    }

    override fun executePagingApiCall() {
        viewModel.getTopRatedMoviesList()
    }

    override fun executeApiCall() {}

    override fun getLayoutResource(): Int {
        return R.layout.top_rated_fragment
    }

    private fun setAdapter() {
        topRatedMoviesAdapter = object : RVAdapterWithPaging<Result, ItemPhotosListBinding>(
            DIFF_CALLBACK, R.layout.item_photos_list, BR.result
        ) {
            override fun onBind(binding: ItemPhotosListBinding, item: Result, position: Int) {
                super.onBind(binding, item, position)
                parentActivity?.loadImage(
                    POSTER_BASE_URL + item.poster_path, binding.ivPhoto, binding.progressBar
                )
                binding.cvImage.setOnClickListener(DoubleClickListener(parentActivity?.applicationContext) {
                    val bundle = Bundle()
                    bundle.putInt("movieId", item.id!!)
                    binding.cvImage.navigateWithId(R.id.topRatedToMovieDetail,bundle)
                })
            }
        }
        binding.apply {
            rvTopRated.layoutManager = GridLayoutManager(parentActivity?.applicationContext, 2)
            rvTopRated.adapter = topRatedMoviesAdapter
            rvTopRated.adapter =
                topRatedMoviesAdapter.withLoadStateHeaderAndFooter(LoadMoreAdapter { topRatedMoviesAdapter.retry() },
                    LoadMoreAdapter { topRatedMoviesAdapter.retry() })
        }

    }

    private fun setObserver() {
        viewModel.topRatedMovies.customCollector(this,
            onLoading = ::onLoading,
            onError = ::onError,
            onSuccess = {
                lifecycleScope.launch { topRatedMoviesAdapter.submitData(it) }
            })
    }

    override fun onNetworkChanged(status: Boolean?) {
        super.onNetworkChanged(status)
        if (status == true) topRatedMoviesAdapter.retry()

    }
}