package com.example.koltinflowex.presentation.views

import android.content.Intent
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.api.POSTER_BASE_URL
import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.PhotosResponse
import com.example.koltinflowex.data.model.Result
import com.example.koltinflowex.databinding.ActivityMainBinding
import com.example.koltinflowex.databinding.ItemCommentBinding
import com.example.koltinflowex.databinding.ItemPhotosListBinding
import com.example.koltinflowex.presentation.common.adapter.LoadMoreAdapter
import com.example.koltinflowex.presentation.common.adapter.RVAdapter
import com.example.koltinflowex.presentation.common.adapter.RVAdapterWithPaging
import com.example.koltinflowex.presentation.common.base.BaseActivity
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import com.example.koltinflowex.presentation.common.base.DoubleClickListener
import com.example.koltinflowex.presentation.common.customCollector
import com.example.koltinflowex.presentation.common.loadImage
import com.example.koltinflowex.presentation.common.multifab.MiniFabAdapter
import com.example.koltinflowex.presentation.common.multifab.MultiFabItem
import com.example.koltinflowex.presentation.views.moviedetailactivity.MovieDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), MiniFabAdapter.OnFabItemClickListener {
    private val viewModel: ViewModel by viewModels()
    private val items = listOf(MultiFabItem(1, R.drawable.add), MultiFabItem(2, R.drawable.add))

    var searchedText = ""
    private lateinit var commentAdapter: RVAdapter<CommentModel, ItemCommentBinding>
    private lateinit var memeAdapter: RVAdapter<MemeResponse, ItemPhotosListBinding>
    private lateinit var photosAdapter: RVAdapter<PhotosResponse, ItemPhotosListBinding>
    private lateinit var moviesListAdapter: RVAdapter<Result, ItemPhotosListBinding>

    private lateinit var movieAdapter: RVAdapterWithPaging<Result, ItemPhotosListBinding>

    private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
            return oldItem == newItem
        }
    }


    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


    override fun executeApiCall() {/*  searchedText = binding.searchEditText.text.toString()
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

    override fun executePagingApiCall() {
        viewModel.getMoviesList()
    }

    override fun onCreateView() {
        binding.multiFab.setMiniFabItems(items)
        binding.multiFab.setOnFabItemClickListener(this)
        setAdapter()
        setObserver()
    }

    private fun setObserver() {
        viewModel.movieList.customCollector(
            this@MainActivity, onLoading = ::onLoading, onSuccess = {
                binding.rvComments.removeAllViews()
                lifecycleScope.launch { movieAdapter.submitData(it) }
            }, onError = ::onError
        )
        viewModel.allCommentsFlow.customCollector(
            this@MainActivity,
            onLoading = ::onLoading,
            onSuccess = { commentAdapter.list = it },
            onError = ::onError
        )
        viewModel.searchCommentStateFlow.customCollector(
            this@MainActivity, onLoading = ::onLoading, onSuccess = {
                it.let { comment ->
                    Log.e("Comment-->", "setObserver: $comment")
                    commentAdapter.clearList()
                    commentAdapter.addData(comment)
                    commentAdapter.notifyDataSetChanged()
                }
            }, onError = ::onError
        )
        viewModel.allPhotos.customCollector(
            this@MainActivity,
            ::onLoading,
            onSuccess = { photosAdapter.list = it.reversed() },
            ::onError
        )
        viewModel.allMeme.customCollector(
            this@MainActivity, ::onLoading, onSuccess = { memeAdapter.list = it }, ::onError
        )
    }

    private fun setAdapter() {
        /** COMMENT ADAPTER */
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
                            this@MainActivity, bean.id.toString(), Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        binding.rvComments.layoutManager = LinearLayoutManager(this)
        binding.rvComments.adapter = commentAdapter
        /** PHOTOS ADAPTER */
        photosAdapter = object :
            RVAdapter<PhotosResponse, ItemPhotosListBinding>(R.layout.item_photos_list, 1) {
            override fun onBind(
                binding: ItemPhotosListBinding, bean: PhotosResponse, position: Int
            ) {
                super.onBind(binding, bean, position)
                // binding.tvId.text = bean.id.toString()
                Glide.with(this@MainActivity).load(bean.url)
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
        binding.rvComments.layoutManager = LinearLayoutManager(this)
        binding.rvComments.adapter = photosAdapter

        /** MEME ADAPTER */
        memeAdapter =
            object : RVAdapter<MemeResponse, ItemPhotosListBinding>(R.layout.item_photos_list, 1) {
                override fun onBind(
                    binding: ItemPhotosListBinding, bean: MemeResponse, position: Int
                ) {
                    super.onBind(binding, bean, position)
                    // binding.tvId.text = bean.id.toString()
                    Glide.with(this@MainActivity).load(bean.image)
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
        binding.rvComments.layoutManager = LinearLayoutManager(this)
        binding.rvComments.adapter = memeAdapter
        /** MOVIESlIST WITHOUT PAGINATION*/
        moviesListAdapter = object : RVAdapter<Result, ItemPhotosListBinding>(
            R.layout.item_photos_list, 1
        ) {
            override fun onBind(
                binding: ItemPhotosListBinding, bean: Result, position: Int
            ) {
                super.onBind(binding, bean, position)
                this@MainActivity.loadImage(
                    POSTER_BASE_URL + bean.posterPath, binding.ivPhoto, binding.progressBar
                )
            }
        }


        /** MOVIESlIST WITH PAGINATION*/
        movieAdapter = object : RVAdapterWithPaging<Result, ItemPhotosListBinding>(
            DIFF_CALLBACK, R.layout.item_photos_list, 1
        ) {
            override fun onBind(binding: ItemPhotosListBinding, item: Result, position: Int) {
                super.onBind(binding, item, position)
                binding.ivPhoto.setOnClickListener(DoubleClickListener {
                    val intent = Intent(this@MainActivity, MovieDetailActivity::class.java)
                    intent.putExtra("movieId", item.id)
                    startActivity(intent)
                })
                this@MainActivity.loadImage(
                    POSTER_BASE_URL + item.posterPath, binding.ivPhoto, binding.progressBar
                )
            }
        }
        binding.rvComments.layoutManager = LinearLayoutManager(this)
        binding.rvComments.adapter = movieAdapter
        binding.rvComments.adapter =
            movieAdapter.withLoadStateHeaderAndFooter(header = LoadMoreAdapter { movieAdapter.retry() },
                footer = LoadMoreAdapter { movieAdapter.retry() })
    }

    override fun onFabItemClick(item: MultiFabItem) {
        when (item.id) {
            1 -> {
                Toast.makeText(this, "1st item clicked", Toast.LENGTH_SHORT).show()
            }

            2 -> {
                Toast.makeText(this, "2nd item clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onNetworkChanged(status: Boolean?) {
        super.onNetworkChanged(status)
        if (status == true) {
            movieAdapter.retry()
        }
    }
}
