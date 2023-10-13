package com.example.koltinflowex.presentation.views.fragment.movielist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.databinding.ItemPhotosListBinding
import com.example.koltinflowex.databinding.MovieListFragmentBinding
import com.example.koltinflowex.presentation.common.adapter.RVAdapter
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.example.koltinflowex.presentation.common.base.RecyclerViewLoadMoreListener
import com.example.koltinflowex.presentation.common.customCollector
import com.example.koltinflowex.presentation.common.loadImage
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MovieListFragment : BaseFragment<MovieListFragmentBinding>(),
    SwipeRefreshLayout.OnRefreshListener {
    private var page: Int = 1
    private val moviesViewModel: MoviesViewModel by viewModels()
    private lateinit var movieListAdapter: RVAdapter<Result, ItemPhotosListBinding>
    private lateinit var recyclerViewLoadMoreListener: RecyclerViewLoadMoreListener

    override fun onCreateView(view: View, saveInstanceState: Bundle?) {
        setAdapter()
        setRecyclerViewLoadMore()
        reloadPage()
    }

    override fun setObserver() {
        moviesViewModel.popularMovies.customCollector(
            this@MovieListFragment,
            onLoading = ::onLoading,
            onSuccess = {
                if (it.results != null) {
                    movieListAdapter.addToList(it.results)
                    recyclerViewLoadMoreListener.updateStateAfterGetData(it.total_pages)
                }
                recyclerViewLoadMoreListener.hideRecyclerViewLoading()
                movieListAdapter.notifyDataSetChanged()

            },
            onError = ::onError
        )
    }

    override fun initViews() {

    }

    override fun getLayoutResource(): Int {
        return R.layout.movie_list_fragment
    }

    private fun createRecyclerViewLoadMoreListener(): RecyclerViewLoadMoreListener {
        return object :
            RecyclerViewLoadMoreListener(mainbinding.rvComments.layoutManager as GridLayoutManager) {
            override fun loadMoreData(currentPage: Int) {
                page = currentPage
                moviesViewModel.getPopularMovies(page)
            }
        }
    }

    private fun setRecyclerViewLoadMore() {
        recyclerViewLoadMoreListener = createRecyclerViewLoadMoreListener()
        mainbinding.rvComments.addOnScrollListener(recyclerViewLoadMoreListener)
    }

    private fun setAdapter() {
        movieListAdapter = object : RVAdapter<Result, ItemPhotosListBinding>(
            R.layout.item_photos_list, 1
        ) {
            override fun onBind(binding: ItemPhotosListBinding, bean: Result, position: Int) {
                super.onBind(binding, bean, position)
                parentActivity?.loadImage(
                    POSTER_BASE_URL + bean.poster_path, binding.ivPhoto
                )
                binding.tvMovieName.text = bean.title

                binding.cvImage.setOnClickListener {
                    val bundle = Bundle()
                    bundle.putInt("movieId", bean.id!!)
                    binding.cvImage.navigateWithId(
                        R.id.movieListToMovieDetail,
                        parentActivity?.navController?.currentDestination?.id,
                        bundle
                    )
                }
            }

        }
        val itemAnimator: SimpleItemAnimator =
            mainbinding.rvComments.itemAnimator as SimpleItemAnimator
        itemAnimator.supportsChangeAnimations = false
        mainbinding.rvComments.layoutManager =
            GridLayoutManager(parentActivity?.applicationContext, 3)
        mainbinding.rvComments.adapter = movieListAdapter

    }

    private fun reloadPage() {
        recyclerViewLoadMoreListener.resetState()
        page = 1
        moviesViewModel.getPopularMovies(1)
    }

    override fun onRefresh() {
        reloadPage()
    }

    override fun onNetworkChanged(status: Boolean?) {
        super.onNetworkChanged(status)
        if (status == true) {
            moviesViewModel.getPopularMovies(page)
        }
    }
}