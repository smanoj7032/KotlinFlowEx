package com.example.koltinflowex.presentation.views.activity

import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import com.example.koltinflowex.R
import com.example.koltinflowex.databinding.ActivityMainBinding
import com.example.koltinflowex.presentation.common.base.BaseActivity
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import com.example.koltinflowex.presentation.common.hideKeyBoard
import com.example.koltinflowex.presentation.common.multifab.MiniFabAdapter
import com.example.koltinflowex.presentation.common.multifab.MultiFabItem
import com.example.koltinflowex.presentation.views.fragment.movielist.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), MiniFabAdapter.OnFabItemClickListener,
    View.OnClickListener {

    private val moviesViewModel: MoviesViewModel by viewModels()
    private val items = listOf(MultiFabItem(1, R.drawable.add), MultiFabItem(2, R.drawable.add))

    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): BaseViewModel {
        return moviesViewModel
    }

    override fun executeApiCall() {}

    override fun executePagingApiCall() {}


    override fun onCreateView() {
        setUpBottomBar()
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movie_list_fragment -> {
                    binding.imdBtnSearch.visibility = View.VISIBLE
                    binding.imgBtnBack.visibility = View.GONE
                    showBottomNav()
                }

                R.id.top_rated_fragment -> {
                    binding.imdBtnSearch.visibility = View.VISIBLE
                    binding.imgBtnBack.visibility = View.GONE
                    showBottomNav()
                }

                R.id.tv_serial_fragment -> {
                    binding.imdBtnSearch.visibility = View.VISIBLE
                    binding.imgBtnBack.visibility = View.GONE
                    showBottomNav()
                }

                R.id.setting_fragment -> {
                    binding.imdBtnSearch.visibility = View.GONE
                    binding.imgBtnBack.visibility = View.GONE
                    showBottomNav()
                }

                else -> {
                    hideBottomNav()
                    binding.imdBtnSearch.visibility = View.GONE
                    binding.imgBtnBack.visibility = View.VISIBLE
                }
            }
        }
        binding.multiFab.setMiniFabItems(items)
        binding.imdBtnSearch.setOnClickListener(this)
        binding.imgBtnBack.setOnClickListener(this)
        binding.ivClearSearch.setOnClickListener(this)
        binding.searchEditText.setOnEditorActionListener { _, p1, _ ->
            if (p1 == EditorInfo.IME_ACTION_DONE) {
                hideSearchEditText()
                return@setOnEditorActionListener true
            } else {
                return@setOnEditorActionListener false
            }
        }
        binding.multiFab.setOnFabItemClickListener(this)
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

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.imd_btn_search -> {
                showSearchEditText()
            }

            R.id.img_btn_back -> {
                navController.navigateUp()
            }

            R.id.iv_clear_search -> {
                binding.searchEditText.text?.clear()
                hideSearchEditText()
            }
        }
    }

    private fun showSearchEditText() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_in)
        binding.searchEditText.visibility = View.VISIBLE
        binding.ivClearSearch.visibility = View.VISIBLE
        binding.searchEditText.isEnabled = true
        binding.searchEditText.requestFocus()
        binding.searchEditText.startAnimation(animation)
    }

    private fun hideSearchEditText() {
        hideKeyBoard()
        val animation = AnimationUtils.loadAnimation(this, R.anim.slide_out)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.searchEditText.visibility = View.GONE
                binding.ivClearSearch.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }

        })
        binding.searchEditText.startAnimation(animation)
    }
}
