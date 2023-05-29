package com.example.koltinflowex.presentation.views

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.View.OnClickListener
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.koltinflowex.R
import com.example.koltinflowex.data.model.CommentModel
import com.example.koltinflowex.data.model.MemeResponse
import com.example.koltinflowex.data.model.PhotosResponse
import com.example.koltinflowex.databinding.ActivityMainBinding
import com.example.koltinflowex.databinding.ItemCommentBinding
import com.example.koltinflowex.databinding.ItemPhotosListBinding
import com.example.koltinflowex.domain.network.helper.Status
import com.example.koltinflowex.presentation.common.adapter.RVAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), OnClickListener {
    private val viewModel: ViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private lateinit var commentAdapter: RVAdapter<CommentModel, ItemCommentBinding>
    private lateinit var memeAdapter: RVAdapter<MemeResponse, ItemPhotosListBinding>
    private lateinit var photosAdapter: RVAdapter<PhotosResponse, ItemPhotosListBinding>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener(this)
        setAdapter()
        setObserver()
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button -> {
                if (binding.searchEditText.text.isNullOrEmpty()) {
                   viewModel.getAllComment()
                } else {
                    viewModel.getNewComment(binding.searchEditText.text.toString().toInt())
                }
            }
        }
    }

    private fun setObserver() = CoroutineScope(Dispatchers.IO).launch {
        lifecycleScope.launch {
            viewModel.allCommentsFlow.collect {
                when (it.status) {
                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        binding.rvComments.visibility = View.VISIBLE
                        commentAdapter.list = it.data
                        Log.e("AllComments-->", "setObserver: ${it.data}")
                    }

                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    else -> {
                        Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT)
                            .show()
                        Log.e("ERROR-->", "setObserver: ${it.message}")
                    }
                }
            }
        }
        lifecycleScope.launch {
            viewModel.searchCommentStateFlow.collect {
                when (it.status) {
                    Status.LOADING -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    Status.SUCCESS -> {
                        binding.progressBar.visibility = View.GONE
                        it.data?.let { comment ->
                            Log.e("Comment-->", "setObserver: $comment")
                            commentAdapter.clearList()
                            commentAdapter.addData(comment)
                            commentAdapter.notifyDataSetChanged()
                        }
                    }

                    else -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@MainActivity, "${it.message}", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }
        }
        /* lifecycleScope.launch {
             viewModel.allPhotos.collect {
                 when (it.status) {
                     Status.LOADING -> {
                         binding.progressBar.visibility = View.VISIBLE
                     }

                     Status.SUCCESS -> {
                         binding.progressBar.visibility = View.GONE
                         photosAdapter.list = it.data?.reversed()
                         Log.e("AllComments-->", "setObserver: ${it.data}")
                     }

                     else -> {
                         Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                     }
                 }
             }
         }
         lifecycleScope.launch {
             viewModel.allMeme.collect {
                 when (it.status) {
                     Status.LOADING -> {
                         binding.progressBar.visibility = View.VISIBLE
                     }

                     Status.SUCCESS -> {
                         binding.progressBar.visibility = View.GONE
                         memeAdapter.list = it.data
                         Log.e("TAG==>", "setObserver: ${it.data}")
                     }

                     else -> {
                         Toast.makeText(this@MainActivity, it.message, Toast.LENGTH_SHORT).show()
                     }
                 }
             }
         }*/
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

        /* */
        /** PHOTOS ADAPTER *//*

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


        */
        /** MEME ADAPTER *//*
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
        binding.rvComments.adapter = memeAdapter*/
    }
}