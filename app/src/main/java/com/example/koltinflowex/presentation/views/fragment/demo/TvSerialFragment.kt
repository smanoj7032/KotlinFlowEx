package com.example.koltinflowex.presentation.views.fragment.demo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.koltinflowex.BR
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.databinding.ItemPhotosListBinding
import com.example.koltinflowex.databinding.TvSerialFragmentBinding
import com.example.koltinflowex.presentation.common.adapter.LoadMoreAdapter
import com.example.koltinflowex.presentation.common.adapter.RVAdapterWithPaging
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.example.koltinflowex.presentation.common.customCollector
import com.example.koltinflowex.presentation.common.loadImage
import com.example.koltinflowex.presentation.views.fragment.movielist.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TvSerialFragment : BaseFragment<TvSerialFragmentBinding>() {
    private val moviesViewModel: MoviesViewModel by viewModels()
    private var searchedText = ""
    private lateinit var adapter: RVAdapterWithPaging<Result, ItemPhotosListBinding>

    override fun onCreateView(view: View, saveInstanceState: Bundle?) {
        setAdapter()
        setObserver()
    }

    override fun executeApiCall() {lifecycleScope.launch { moviesViewModel.getUpcomingMoviesList() }}
    override fun initViews() {
        val searchBar = parentActivity?.findViewById<EditText>(R.id.search_edit_text)
        searchBar?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchedText = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (searchedText == "") {
                    lifecycleScope.launch { moviesViewModel.getUpcomingMoviesList() }
                }
            }
        })
        searchBar?.setOnEditorActionListener { p0, p1, p2 ->
            if (searchedText != "" && p1 == EditorInfo.IME_ACTION_DONE) {
                lifecycleScope.launch { moviesViewModel.getUpcomingMoviesList() }
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }    }

    override fun getLayoutResource(): Int {
        return R.layout.tv_serial_fragment
    }

    private fun setObserver() {
        moviesViewModel.upComingMovies.customCollector(
            this@TvSerialFragment,
            onLoading = ::onLoading,
            onSuccess = { lifecycleScope.launch { adapter.submitData(it) } },
            onError = ::onError
        )
    }

    private fun setAdapter() {
        adapter = object : RVAdapterWithPaging<Result, ItemPhotosListBinding>(
            DIFF_CALLBACK, R.layout.item_photos_list, BR.result
        ) {
            override fun onBind(binding: ItemPhotosListBinding, item: Result, position: Int) {
                super.onBind(binding, item, position)
                parentActivity?.loadImage(POSTER_BASE_URL + item.poster_path, binding.ivPhoto)
                binding.cvImage.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("movieId", item.id!!)
                    binding.cvImage.navigateWithId(
                        R.id.tvSerialToMovieDetail,
                        parentActivity?.navController?.currentDestination?.id,
                        bundle
                    )
                }
            }
        }
        mainbinding.apply {
            rvTopRated.layoutManager = GridLayoutManager(parentActivity?.applicationContext, 2)
            rvTopRated.adapter = adapter
            rvTopRated.adapter =
                adapter.withLoadStateHeaderAndFooter(LoadMoreAdapter { adapter.retry() },
                    LoadMoreAdapter { adapter.retry() })
        }
        adapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                onLoading(true)
            } else {
                onLoading(false)
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    onError(Throwable(it.error.message), true)
                }
            }
        }
    }
}