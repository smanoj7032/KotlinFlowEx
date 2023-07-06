package com.example.koltinflowex.presentation.views.fragment.movielist

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.databinding.ItemPhotosListBinding
import com.example.koltinflowex.databinding.MovieListFragmentBinding
import com.example.koltinflowex.presentation.common.adapter.LoadMoreAdapter
import com.example.koltinflowex.presentation.common.adapter.RVAdapter
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
    private var popularMovies: PagingData<Result>? = null
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    var searchedText = ""
    private lateinit var moviesListAdapter: RVAdapter<Result, ItemPhotosListBinding>


    override fun onCreateView(view: View, saveInstanceState: Bundle?) {
        setAdapter()
        setObserver()
        val searchBar = parentActivity?.findViewById<EditText>(R.id.search_edit_text)
        searchBar?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchedText = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (searchedText == "") {
                    popularMovies = null
                    moviesViewModel.getPopularMovies()
                }
            }
        })
        searchBar?.setOnEditorActionListener { p0, p1, p2 ->
            if (searchedText != "" && p1 == EditorInfo.IME_ACTION_DONE) {
                moviesViewModel.getSearchMovie(searchedText,"popularMovies")
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
    }

    override fun executePagingApiCall() {
        moviesViewModel.getPopularMovies()
    }

    override fun executeApiCall() {
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
                    binding.cvImage.navigateWithId(R.id.movieListToMovieDetail,parentActivity?.navController?.currentDestination?.id, bundle)
                }
            }

        }
        mainbinding.rvComments.layoutManager = GridLayoutManager(parentActivity?.applicationContext, 2)
        mainbinding.rvComments.adapter = movieListAdapter
        mainbinding.rvComments.adapter =
            movieListAdapter.withLoadStateHeaderAndFooter(header = LoadMoreAdapter { movieListAdapter.retry() },
                footer = LoadMoreAdapter { movieListAdapter.retry() })

        /** MOVIESlIST WITHOUT PAGINATION*//*
        moviesListAdapter = object : RVAdapter<Result, ItemPhotosListBinding>(
            R.layout.item_photos_list, 1
        ) {
            override fun onBind(
                binding: ItemPhotosListBinding, bean: Result, position: Int
            ) {
                super.onBind(binding, bean, position)
                parentActivity?.applicationContext?.loadImage(
                    POSTER_BASE_URL + bean.posterPath, binding.ivPhoto, binding.progressBar
                )
            }
        }*/

    }

    private fun setObserver() {
        moviesViewModel.popularMovies.customCollector(
            this, onLoading = ::onLoading, onSuccess = {
                popularMovies = it
                lifecycleScope.launch {
                    movieListAdapter.submitData(popularMovies!!)
                }
            }, onError = ::onError
        )
    }

    override fun onNetworkChanged(status: Boolean?) {
        super.onNetworkChanged(status)
        if (status == true) {
            movieListAdapter.retry()
        }
    }
}