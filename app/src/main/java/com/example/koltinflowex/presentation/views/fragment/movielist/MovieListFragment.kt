package com.example.koltinflowex.presentation.views.fragment.movielist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.PhotosResponse
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.databinding.ItemCommentBinding
import com.example.koltinflowex.databinding.ItemPhotosListBinding
import com.example.koltinflowex.databinding.MovieListFragmentBinding
import com.example.koltinflowex.presentation.common.adapter.LoadMoreAdapter
import com.example.koltinflowex.presentation.common.adapter.RVAdapter
import com.example.koltinflowex.presentation.common.adapter.RVAdapterWithPaging
import com.example.koltinflowex.presentation.common.base.BaseFragment
import com.example.koltinflowex.presentation.common.base.DoubleClickListener
import com.example.koltinflowex.presentation.common.customCollector
import com.example.koltinflowex.presentation.common.loadImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : BaseFragment<MovieListFragmentBinding>() {
    private val viewModel: ViewModel by viewModels()
    private lateinit var movieListAdapter: RVAdapterWithPaging<Result, ItemPhotosListBinding>
    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }

    var searchedText = ""
    private lateinit var commentAdapter: RVAdapter<CommentModel, ItemCommentBinding>
    private lateinit var memeAdapter: RVAdapter<MemeResponse, ItemPhotosListBinding>
    private lateinit var photosAdapter: RVAdapter<PhotosResponse, ItemPhotosListBinding>
    private lateinit var moviesListAdapter: RVAdapter<Result, ItemPhotosListBinding>


    override fun onCreateView(view: View, saveInstanceState: Bundle?) {
        setAdapter()
        setObserver()
    }

    override fun executePagingApiCall() {
        viewModel.getMoviesList()
    }

    override fun executeApiCall() {   /*  searchedText = binding.searchEditText.text.toString()
          if (searchedText == "") viewModel.getAllComment()
          else viewModel.getNewComment(searchedText.toInt())
          binding.searchEditText.addTextChangedListener(object : TextWatcher {
              override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

              }

              override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

              }

              override fun afterTextChanged(p0: Editable?) {
                  if (p0.isNullOrEmpty()) {
                      viewModel.getAllComment()
                  } else {
                      viewModel.getNewComment(binding.searchEditText.text.toString().toInt())
                  }
              }

          })
          viewModel.getMoviesList2()*/
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
                    POSTER_BASE_URL + item.poster_path, binding.ivPhoto, binding.progressBar
                )

                binding.cvImage.setOnClickListener(DoubleClickListener(parentActivity?.applicationContext) {
                    val bundle = Bundle()
                    bundle.putInt("movieId", item.id!!)
                    binding.cvImage.navigateWithId(R.id.movieListToMovieDetail, bundle)
                })
            }

        }
        binding.rvComments.layoutManager = GridLayoutManager(parentActivity?.applicationContext, 2)
        binding.rvComments.adapter = movieListAdapter
        binding.rvComments.adapter =
            movieListAdapter.withLoadStateHeaderAndFooter(header = LoadMoreAdapter { movieListAdapter.retry() },
                footer = LoadMoreAdapter { movieListAdapter.retry() })


 /*       *//** COMMENT ADAPTER *//*
        commentAdapter =
            object : RVAdapter<CommentModel, ItemCommentBinding>(R.layout.item_comment, 1) {
                override fun onBind(
                    binding: ItemCommentBinding, bean: CommentModel, position: Int
                ) {
                    super.onBind(binding, bean, position)
                    binding.tvCommentData.text = bean.body
                    binding.tvPostData.text = bean.id.toString()
                    binding.clLayComments.setOnClickListener {
                        Toast.makeText(
                            parentActivity?.applicationContext,
                            bean.id.toString(),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        binding.rvComments.layoutManager = LinearLayoutManager(parentActivity?.applicationContext)
        binding.rvComments.adapter = commentAdapter
        *//** PHOTOS ADAPTER *//*
        photosAdapter = object :
            RVAdapter<PhotosResponse, ItemPhotosListBinding>(R.layout.item_photos_list, 1) {
            override fun onBind(
                binding: ItemPhotosListBinding, bean: PhotosResponse, position: Int
            ) {
                super.onBind(binding, bean, position)
                // binding.tvId.text = bean.id.toString()
                Glide.with(this@MovieListFragment).load(bean.url)
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable, transition: Transition<in Drawable>?
                        ) {
                            binding.progressBar.visibility = View.GONE
                            binding.ivPhoto.setImageDrawable(resource)
                        }

                        override fun onLoadStarted(placeholder: Drawable?) {
                            super.onLoadStarted(placeholder)
                            binding.progressBar.visibility = View.VISIBLE
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {}
                    })
            }
        }
        binding.rvComments.layoutManager = LinearLayoutManager(parentActivity?.applicationContext)
        binding.rvComments.adapter = photosAdapter

        *//** MEME ADAPTER *//*
        memeAdapter =
            object : RVAdapter<MemeResponse, ItemPhotosListBinding>(R.layout.item_photos_list, 1) {
                override fun onBind(
                    binding: ItemPhotosListBinding, bean: MemeResponse, position: Int
                ) {
                    super.onBind(binding, bean, position)
                    // binding.tvId.text = bean.id.toString()
                    Glide.with(this@MovieListFragment).load(bean.image)
                        .into(object : CustomTarget<Drawable>() {
                            override fun onResourceReady(
                                resource: Drawable, transition: Transition<in Drawable>?
                            ) {
                                binding.progressBar.visibility = View.GONE
                                binding.ivPhoto.setImageDrawable(resource)
                            }

                            override fun onLoadStarted(placeholder: Drawable?) {
                                super.onLoadStarted(placeholder)
                                binding.progressBar.visibility = View.VISIBLE
                            }

                            override fun onLoadCleared(placeholder: Drawable?) {}
                        })
                }
            }
        binding.rvComments.layoutManager = LinearLayoutManager(parentActivity?.applicationContext)
        binding.rvComments.adapter = memeAdapter
        *//** MOVIESlIST WITHOUT PAGINATION*//*
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
        viewModel.popularMovies.customCollector(
            this, onLoading = ::onLoading, onSuccess = {
                binding.rvComments.removeAllViews()
                lifecycleScope.launch { movieListAdapter.submitData(it) }
            }, onError = ::onError
        )

     /*   viewModel.allCommentsFlow.customCollector(
            this,
            onLoading = ::onLoading,
            onSuccess = { commentAdapter.list = it },
            onError = ::onError
        )
        viewModel.searchCommentStateFlow.customCollector(
            this, onLoading = ::onLoading, onSuccess = {
                it.let { comment ->
                    Log.e("Comment-->", "setObserver: $comment")
                    commentAdapter.clearList()
                    commentAdapter.addData(comment)
                    commentAdapter.notifyDataSetChanged()
                }
            }, onError = ::onError
        )
        viewModel.allPhotos.customCollector(
            this, ::onLoading, onSuccess = { photosAdapter.list = it.reversed() }, ::onError
        )
        viewModel.allMeme.customCollector(
            this, ::onLoading, onSuccess = { memeAdapter.list = it }, ::onError
        )*/

    }

    override fun onNetworkChanged(status: Boolean?) {
        super.onNetworkChanged(status)
        if (status == true) {
            movieListAdapter.retry()
        }
    }
}