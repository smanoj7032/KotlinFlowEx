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
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.databinding.ItemPhotosListBinding
import com.example.koltinflowex.databinding.MovieListFragmentBinding
import com.example.koltinflowex.presentation.common.adapter.LoadMoreAdapter
import com.example.koltinflowex.presentation.common.adapter.RVAdapterWithPaging
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.example.koltinflowex.presentation.common.customCollector
import com.example.koltinflowex.presentation.common.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MovieListFragment : BaseFragment<MovieListFragmentBinding>() {
    private val moviesViewModel: MoviesViewModel by viewModels()
    private lateinit var movieListAdapter: RVAdapterWithPaging<Result, ItemPhotosListBinding>
    var searchedText = ""


    override fun onCreateView(view: View, saveInstanceState: Bundle?) {
        setAdapter()
        setObserver()
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
                    lifecycleScope.launch { moviesViewModel.getPopularMovies() }
                }
            }
        })
        searchBar?.setOnEditorActionListener { p0, p1, p2 ->
            if (searchedText != "" && p1 == EditorInfo.IME_ACTION_DONE) {
                lifecycleScope.launch {
                    moviesViewModel.getSearchMovie(searchedText, "popular")
                }
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }

    override fun executeApiCall() {
        lifecycleScope.launch { moviesViewModel.getPopularMovies() }
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
        mainbinding.rvComments.layoutManager =
            GridLayoutManager(parentActivity?.applicationContext, 2)
        mainbinding.rvComments.adapter = movieListAdapter
        mainbinding.rvComments.adapter =
            movieListAdapter.withLoadStateHeaderAndFooter(header = LoadMoreAdapter { movieListAdapter.retry() },
                footer = LoadMoreAdapter { movieListAdapter.retry() })

        movieListAdapter.addLoadStateListener { loadState ->
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

    private fun setObserver() {
        moviesViewModel.popularMovies.customCollector(
            this@MovieListFragment,
            onLoading = ::onLoading,
            onSuccess = { lifecycleScope.launch { movieListAdapter.submitData(it) } },
            onError = ::onError
        )
    }


    override fun onNetworkChanged(status: Boolean?) {
        super.onNetworkChanged(status)
        if (status == true) {
            movieListAdapter.retry()
        }
    }
}