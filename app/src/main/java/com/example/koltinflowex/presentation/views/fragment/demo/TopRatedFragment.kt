package com.example.koltinflowex.presentation.views.fragment.demo

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
import com.example.koltinflowex.BR
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.databinding.ItemPhotosListBinding
import com.example.koltinflowex.databinding.TopRatedFragmentBinding
import com.example.koltinflowex.presentation.common.adapter.RVAdapter
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.example.koltinflowex.presentation.common.customCollector
import com.example.koltinflowex.presentation.common.loadImage
import com.example.koltinflowex.presentation.views.fragment.movielist.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TopRatedFragment : BaseFragment<TopRatedFragmentBinding>() {
    private val moviesViewModel: MoviesViewModel by viewModels()
    private lateinit var topRatedMoviesAdapter: RVAdapter<Result, ItemPhotosListBinding>
    private var searchedText = ""
    private var popularMovies: PagingData<Result>? = null


    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateView(view: View, saveInstanceState: Bundle?) {
        val searchBar = parentActivity?.findViewById<EditText>(R.id.search_edit_text)
        searchBar?.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                searchedText = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {
                if (searchedText == "") {
                    popularMovies = null
                    moviesViewModel.getTopRatedMoviesList()
                }
            }
        })
        searchBar?.setOnEditorActionListener { p0, p1, p2 ->
            if (searchedText != "" && p1 == EditorInfo.IME_ACTION_DONE) {
                moviesViewModel.getSearchMovie(searchedText, "topRated")
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
        setAdapter()
        setObserver()
    }

    override fun executePagingApiCall() {
        moviesViewModel.getTopRatedMoviesList()
    }

    override fun executeApiCall() {}

    override fun getLayoutResource(): Int {
        return R.layout.top_rated_fragment
    }

    private fun setAdapter() {
        topRatedMoviesAdapter = object : RVAdapter<Result, ItemPhotosListBinding>(
            R.layout.item_photos_list, BR.result
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
                        R.id.topRatedToMovieDetail,
                        parentActivity?.navController?.currentDestination?.id,
                        bundle
                    )
                }
            }
        }
        mainbinding.apply {
            rvTopRated.layoutManager = GridLayoutManager(parentActivity?.applicationContext, 2)
            rvTopRated.adapter = topRatedMoviesAdapter
        }

    }

    private fun setObserver() {
        moviesViewModel.topRatedMovies.customCollector(this,
            onLoading = ::onLoading,
            onError = ::onError,
            onSuccess = {
                lifecycleScope.launch { topRatedMoviesAdapter.list = it.results }
            })
    }

    override fun onNetworkChanged(status: Boolean?) {
        super.onNetworkChanged(status)

    }
}