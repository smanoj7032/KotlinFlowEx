package com.example.koltinflowex.presentation.views.activity

import android.widget.Toast
import androidx.activity.viewModels
import com.example.koltinflowex.R
import com.example.koltinflowex.databinding.ActivityMainBinding
import com.example.koltinflowex.presentation.common.base.BaseActivity
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import com.example.koltinflowex.presentation.common.multifab.MiniFabAdapter
import com.example.koltinflowex.presentation.common.multifab.MultiFabItem
import com.example.koltinflowex.presentation.views.fragment.movielist.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), MiniFabAdapter.OnFabItemClickListener {
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
        binding.multiFab.setMiniFabItems(items)
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
}
