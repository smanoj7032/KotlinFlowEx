package com.example.koltinflowex.presentation.views.fragment.movielist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.api.POPULAR_MOVIES
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.databinding.ItemPhotosListBinding
import com.example.koltinflowex.databinding.MovieListFragmentBinding
import com.example.koltinflowex.presentation.common.adapter.LoadMoreAdapter
import com.example.koltinflowex.presentation.common.adapter.RVAdapterWithPaging
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.example.koltinflowex.presentation.common.customCollector
import com.example.koltinflowex.presentation.common.isNetworkAvailable
import com.example.koltinflowex.presentation.common.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieListFragment : BaseFragment<MovieListFragmentBinding>() {
    private val moviesViewModel: MoviesViewModel by viewModels()
    private lateinit var movieListAdapter: RVAdapterWithPaging<Result, ItemPhotosListBinding>
    var searchedText = ""
    var isFirstLoad = true

    override fun onCreateView(view: View, saveInstanceState: Bundle?) {
        lifecycleScope.launch {
            if (parentActivity?.isNetworkAvailable() == true)
                moviesViewModel.getPopularMovies()
        }
        setAdapter()
    }

    override fun initViews() {
        val searchBar = parentActivity?.findViewById<EditText>(R.id.search_edit_text)
        searchBar?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchedText = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (searchedText == "") {
                    if (parentActivity?.isNetworkAvailable() == true) {
                        lifecycleScope.launch {
                            if (parentActivity?.isNetworkAvailable() == true)
                                moviesViewModel.getPopularMovies()
                        }
                    } else {
                        onError(Throwable("check internet connection"), true)
                    }
                }
            }
        })
        searchBar?.setOnEditorActionListener { p0, p1, p2 ->
            if (searchedText != "" && p1 == EditorInfo.IME_ACTION_DONE) {
                lifecycleScope.launch {
                    if (parentActivity?.isNetworkAvailable() == true)
                    moviesViewModel.getSearchMovie(searchedText, POPULAR_MOVIES)
                }
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }

    override fun setObserver() {
        moviesViewModel.popularMovies.customCollector(
            this@MovieListFragment,
            onLoading = ::onLoading,
            onSuccess = { lifecycleScope.launch { movieListAdapter.submitData(it) } },
            onError = ::onError
        )
    }

    override fun getLayoutResource(): Int {
        return R.layout.movie_list_fragment
    }

    private fun setAdapter() {
        movieListAdapter = object : RVAdapterWithPaging<Result, ItemPhotosListBinding>(
            DIFF_CALLBACK, R.layout.item_photos_list, 1
        ) {
            override fun onBind(binding: ItemPhotosListBinding, item: Result, position: Int) {
                super.onBind(binding, item, position)
                parentActivity?.loadImage(
                    POSTER_BASE_URL + item.poster_path, binding.ivPhoto
                )
                binding.tvMovieName.text = item.title

                binding.cvImage.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("movieId", item.id!!)
                    binding.cvImage.navigateWithId(
                        R.id.movieListToMovieDetail,
                        parentActivity?.navController?.currentDestination?.id,
                        bundle
                    )
                }
            }

        }
        val layoutManager = GridLayoutManager(parentActivity?.applicationContext, 3)
        val footerAdapter = LoadMoreAdapter { movieListAdapter.retry() }
        val headerAdapter = LoadMoreAdapter { movieListAdapter.retry() }
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return if ((position == movieListAdapter.itemCount) && footerAdapter.itemCount > 0) 3
                else if (movieListAdapter.itemCount == 0 && headerAdapter.itemCount > 0) 3
                else 1
            }
        }
        mainbinding.rvComments.layoutManager = layoutManager
        mainbinding.rvComments.adapter = movieListAdapter

        mainbinding.rvComments.adapter = movieListAdapter.withLoadStateHeaderAndFooter(
            header = headerAdapter, footer = footerAdapter
        )
        movieListAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading || loadState.append is LoadState.Loading) {
                if (isFirstLoad) {
                    onLoading(true)
                    isFirstLoad = false
                }
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


    override fun onNetworkChanged(status: Boolean?) {
        super.onNetworkChanged(status)
        if (status == true) {
            movieListAdapter.retry()
        }
    }
}