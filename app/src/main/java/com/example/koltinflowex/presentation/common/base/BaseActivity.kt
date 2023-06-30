package com.example.koltinflowex.presentation.common.base

import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.ErrorCodes
import com.example.koltinflowex.common.network.NetworkError
import com.example.koltinflowex.common.network.connectionhelper.NetworkChangeCallback
import com.example.koltinflowex.common.network.connectionhelper.NetworkChangeReceiver
import com.example.koltinflowex.presentation.MyApplication
import com.example.koltinflowex.presentation.common.compooundviews.ErrorView
import com.example.koltinflowex.presentation.common.hideKeyBoard
import com.example.koltinflowex.presentation.common.isNetworkAvailable
import com.example.koltinflowex.presentation.common.showToast
import com.example.koltinflowex.presentation.common.utils.AlertManager
import com.google.android.material.bottomnavigation.BottomNavigationView

abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity(),
    NetworkChangeCallback {
    open val onRetry: (() -> Unit)? = null
    lateinit var binding: Binding
    val app: MyApplication
        get() = application as MyApplication
    private var networkChangeReceiver: NetworkChangeReceiver? = null
    lateinit var navController: NavController
    private var isApiHit = false
    private var isFirstTime = false
    private var isBNVHide = false

    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        networkChangeReceiver = NetworkChangeReceiver(this)
        registerReceiver(
            networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        val layout: Int = getLayoutResource()
        binding = DataBindingUtil.setContentView(this, layout)
        onCreateView()
        if (this.isNetworkAvailable()) {
            executeApiCall()
            executePagingApiCall()
            isFirstTime = true
            isApiHit = true
            Log.e("ApiCall--->>", "onNetworkChanged: first")

        } else onError(Throwable("Check internet connection"), false)
    }

    protected abstract fun getLayoutResource(): Int
    protected abstract fun getViewModel(): BaseViewModel
    protected abstract fun executeApiCall()
    protected abstract fun executePagingApiCall()

    //protected abstract fun getViewModel(): BaseViewModel
    protected abstract fun onCreateView()
    override fun onStop() {
        super.onStop()
        hideKeyBoard()
    }

    override fun onDestroy() {
        deleteCacheData()
        super.onDestroy()
        unregisterReceiver(networkChangeReceiver)
    }

    fun onError(error: Throwable, showError: Boolean, action: (() -> Unit?)? = null) {
        if (error is NetworkError) {
            if (showError) {
                val errorView: View? = findViewById(R.id.error_view)
                errorView?.visibility = View.VISIBLE
            }
            when (error.errorCode) {
                ErrorCodes.SESSION_EXPIRED -> {
                    showToast(getString(R.string.session_expired))
                    app.onLogOut()
                }

                else -> {
                    AlertManager.showNegativeAlert(this, error.message) {}
                }
            }
        } else {
            AlertManager.showNegativeAlert(this, error.message, action = action)
        }
    }

    fun onLoading(isLoading: Boolean) {
        val progress: View? = findViewById(R.id.progress_bar)
        progress?.visibility = if (isLoading) View.VISIBLE else View.GONE
        val errorView: ErrorView? = findViewById(R.id.error_view)
        errorView?.visibility = if (isLoading) View.VISIBLE else View.GONE
        errorView?.onRetry = onRetry
    }

    fun navigateBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun onNetworkChanged(status: Boolean?) {
        if (status == true && !isApiHit) {
            executePagingApiCall()
            Log.e("ApiCall--->>", "onNetworkChanged: second")
        }
        if (status == true && isFirstTime) {
            executeApiCall()
        }
        if (status == false) onError(Throwable("Check internet connection"), false)

        isFirstTime = true
    }

    fun setUpBottomBar() = try {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bnv_view)
        NavigationUI.setupWithNavController(
            bottomNav, navController
        )
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.setting_fragment -> showBottomNav()
                R.id.top_rated_fragment -> showBottomNav()
                R.id.movie_list_fragment -> showBottomNav()
                R.id.tv_serial_fragment -> showBottomNav()
                else -> hideBottomNav()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    private fun showBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bnv_view)
        if (isBNVHide){
            val animation = AnimationUtils.loadAnimation(bottomNav.context, R.anim.slide_up)
            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationStart(p0: Animation?) {}

                override fun onAnimationEnd(p0: Animation?) {
                    bottomNav.visibility = View.VISIBLE
                    isBNVHide = false
                }

                override fun onAnimationRepeat(p0: Animation?) {}

            })
            bottomNav.startAnimation(animation)
        }
    }

    private fun hideBottomNav() {
        val bottomNav = findViewById<BottomNavigationView>(R.id.bnv_view)
        val animation = AnimationUtils.loadAnimation(bottomNav.context, R.anim.slide_down)
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(p0: Animation?) {}

            override fun onAnimationEnd(p0: Animation?) {
                bottomNav.visibility = View.GONE
                isBNVHide = true
            }

            override fun onAnimationRepeat(p0: Animation?) {}

        })
        bottomNav.startAnimation(animation)
    }

    open fun deleteCacheData() {
        val cacheDir = this.cacheDir
        val files = cacheDir.listFiles()
        if (files != null) {
            for (file in files) {
                file.delete()
            }
        }
    }
}
