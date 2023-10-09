package com.example.koltinflowex.presentation.views.activity

import android.content.res.Resources
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.koltinflowex.R
import com.example.koltinflowex.databinding.ActivityMainBinding
import com.example.koltinflowex.presentation.common.base.BaseActivity
import com.example.koltinflowex.presentation.common.base.BaseViewModel
import com.example.koltinflowex.presentation.common.bottomnavigation.MeowBottomNavigation
import com.example.koltinflowex.presentation.common.fancydrawer.widgets.DuoDrawerToggle
import com.example.koltinflowex.presentation.common.hideKeyBoard
import com.example.koltinflowex.presentation.common.showToast
import com.example.koltinflowex.presentation.views.fragment.demo.PlayerFragmentAdapter
import com.example.koltinflowex.presentation.views.fragment.movielist.MoviesViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>(), View.OnClickListener {
    private val moviesViewModel: MoviesViewModel by viewModels()


    override fun getLayoutResource(): Int {
        return R.layout.activity_main
    }

    override fun getViewModel(): BaseViewModel {
        return moviesViewModel
    }

    override fun onResume() {
        super.onResume()
        if (binding.floatingActionMenu.isOpened) binding.floatingActionMenu.close(false)
    }

    private fun initDrawer() {
        val screenWidth = Resources.getSystem().displayMetrics.widthPixels
        val desiredPercentageWidth = 0.70
        val desiredWidth = (screenWidth * desiredPercentageWidth).toInt()
        val layoutParams = binding.drawerMenu.layoutParams
        layoutParams.width = desiredWidth
        binding.drawerMenu.layoutParams = layoutParams
        val drawerToggle = DuoDrawerToggle(
            this,
            binding.layDrawer,
            binding.toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        binding.layDrawer.setDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    private fun initViews() {
        setUpBottomBar()
        binding.bnvView.apply {

            add(
                MeowBottomNavigation.Model(
                    ID_HOME,
                    R.drawable.ic_home
                )
            )
            add(
                MeowBottomNavigation.Model(
                    ID_MESSAGE,
                    R.drawable.ic_message
                )
            )
            add(
                MeowBottomNavigation.Model(
                    ID_NOTIFICATION,
                    R.drawable.ic_notifications
                )
            )
            add(
                MeowBottomNavigation.Model(
                    ID_ACCOUNT,
                    R.drawable.ic_settings
                )
            )

            setCount(ID_NOTIFICATION, "12")

            setOnShowListener {
                val name = when (it.id) {
                    ID_HOME -> "HOME"
                    ID_MESSAGE -> "MESSAGE"
                    ID_NOTIFICATION -> "NOTIFICATION"
                    ID_ACCOUNT -> "ACCOUNT"
                    else -> ""
                }

            }

            setOnClickMenuListener {
                when (it.id) {
                    ID_HOME -> navController.navigate(R.id.movie_list_fragment)
                    ID_MESSAGE -> navController.navigate(R.id.top_rated_fragment)
                    ID_NOTIFICATION -> navController.navigate(R.id.tv_serial_fragment)
                    ID_ACCOUNT -> navController.navigate(R.id.setting_fragment)
                }
            }

            setOnReselectListener {
                Toast.makeText(context, "item ${it.id} is reselected.", Toast.LENGTH_LONG).show()
            }

            show(ID_HOME)

        }
        binding.floatingActionMenu.setClosedOnTouchOutside(true)
        binding.fabAddPost1.setOnClickListener {
            showToast("fabAddPost1")
            binding.floatingActionMenu.close(false)
        }
        binding.fabAddPost2.setOnClickListener {
            showToast("fabAddPost2")
            binding.floatingActionMenu.close(false)
        }
        binding.fabAddPost3.setOnClickListener {
            showToast("fabAddPost3")
            binding.floatingActionMenu.close(false)
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.movie_list_fragment -> {
                    binding.viewPager2.visibility = View.GONE
                    binding.imdBtnSearch.visibility = View.VISIBLE
                    binding.imgBtnBack.visibility = View.GONE
                    showBottomNav()
                }

                R.id.top_rated_fragment -> {
                    binding.viewPager2.visibility = View.GONE
                    binding.imdBtnSearch.visibility = View.VISIBLE
                    binding.imgBtnBack.visibility = View.GONE
                    showBottomNav()
                }

                R.id.tv_serial_fragment -> {
                    binding.viewPager2.visibility = View.GONE
                    binding.imdBtnSearch.visibility = View.VISIBLE
                    binding.imgBtnBack.visibility = View.GONE
                    showBottomNav()
                }

                R.id.setting_fragment -> {
                    binding.viewPager2.visibility = View.VISIBLE
                    binding.imdBtnSearch.visibility = View.GONE
                    binding.imgBtnBack.visibility = View.GONE
                    showBottomNav()
                    setUpViewPager()
                }

                else -> {
                    binding.viewPager2.visibility = View.GONE
                    hideBottomNav()
                    binding.imdBtnSearch.visibility = View.GONE
                    binding.imgBtnBack.visibility = View.VISIBLE
                }
            }
        }
        binding.imdBtnSearch.setOnClickListener(this)
        binding.home.setOnClickListener(this)
        binding.hello.setOnClickListener(this)
        binding.profile.setOnClickListener(this)
        binding.products.setOnClickListener(this)
        binding.fav.setOnClickListener(this)
        binding.cart.setOnClickListener(this)
        binding.logout.setOnClickListener(this)
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
    }

    override fun onCreateView() {
        initViews()
        initDrawer()
    }

    private fun setUpViewPager() {
        val mediaList = ArrayList<String>()
        mediaList.add("https://bitdash-a.akamaihd.net/content/MI201109210084_1/m3u8s/f08e80da-bf1d-4e3d-8899-f0f6155f6efa.m3u8")
        mediaList.add("https://fcc3ddae59ed.us-west-2.playback.live-video.net/api/video/v1/us-west-2.893648527354.channel.YtnrVcQbttF0.m3u8")
        mediaList.add("https://bitdash-a.akamaihd.net/content/sintel/hls/playlist.m3u8")
        mediaList.add("https://cph-p2p-msl.akamaized.net/hls/live/2000341/test/master.m3u8")
        mediaList.add("https://demo.unified-streaming.com/k8s/features/stable/video/tears-of-steel/tears-of-steel.ism/.m3u8")
        mediaList.add("https://devstreaming-cdn.apple.com/videos/streaming/examples/img_bipbop_adv_example_fmp4/master.m3u8")
        val adapter = PlayerFragmentAdapter(this, mediaList)
        binding.viewPager2.adapter = adapter
        binding.viewPager2.orientation = ViewPager2.ORIENTATION_VERTICAL
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

            R.id.hello -> {
            }
        }
        binding.layDrawer.closeDrawer()
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
    companion object {
        private const val ID_HOME = 1
        private const val ID_MESSAGE = 2
        private const val ID_NOTIFICATION = 3
        private const val ID_ACCOUNT = 4
    }
}
