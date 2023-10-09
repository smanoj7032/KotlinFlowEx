package com.example.koltinflowex.presentation.common.base

import android.app.KeyguardManager
import android.content.Context
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.annotation.CallSuper
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
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
import com.example.koltinflowex.presentation.common.bottomnavigation.MeowBottomNavigation
import com.example.koltinflowex.presentation.common.compooundviews.ErrorView
import com.example.koltinflowex.presentation.common.hideKeyBoard
import com.example.koltinflowex.presentation.common.isNetworkAvailable
import com.example.koltinflowex.presentation.common.loader.Loader
import com.example.koltinflowex.presentation.common.showToast
import com.example.koltinflowex.presentation.common.utils.AlertManager

abstract class BaseActivity<Binding : ViewDataBinding> : AppCompatActivity(),
    NetworkChangeCallback {
    open val onRetry: (() -> Unit)? = null
    lateinit var binding: Binding
    var isInterNetAvailable = false
    val app: MyApplication
        get() = application as MyApplication
    private var networkChangeReceiver: NetworkChangeReceiver? = null
    lateinit var navController: NavController
    private var isApiHit = false
    private var isFirstTime = false
    private var isBNVHide = false
    private var lineScaleLoader: Loader? = null


    @RequiresApi(Build.VERSION_CODES.P)
    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val layout: Int = getLayoutResource()
        binding = DataBindingUtil.setContentView(this, layout)
        binding.lifecycleOwner = this
        setSupportActionBar(findViewById(R.id.toolbar))
        networkChangeReceiver = NetworkChangeReceiver(this)
        registerReceiver(
            networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
        onCreateView()
        if (this.isNetworkAvailable()) {
            isInterNetAvailable = true
        } else {
            isInterNetAvailable = false
            onError(Throwable("Check internet connection"), false)
        }
    }

    protected abstract fun getLayoutResource(): Int
    protected abstract fun getViewModel(): BaseViewModel

    //protected abstract fun getViewModel(): BaseViewModel
    protected abstract fun onCreateView()
    override fun onStop() {
        super.onStop()
        hideKeyBoard()
    }

    override fun onDestroy() {
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
        status?.let { isInterNetAvailable = it }
    }

    fun setUpBottomBar() = try {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_main) as NavHostFragment
        navController = navHostFragment.navController
    } catch (e: Exception) {
        e.printStackTrace()
    }

    fun showBottomNav() {
        val bottomNav = findViewById<MeowBottomNavigation>(R.id.bnv_view)
        if (isBNVHide) {
            val animation = AnimationUtils.loadAnimation(bottomNav.context, R.anim.slide_up)
            bottomNav.visibility = View.VISIBLE
            isBNVHide = false
            bottomNav.startAnimation(animation)
        }
    }

    fun hideBottomNav() {
        val bottomNav = findViewById<MeowBottomNavigation>(R.id.bnv_view)
        val animation = AnimationUtils.loadAnimation(bottomNav.context, R.anim.slide_down)
        bottomNav.visibility = View.GONE
        isBNVHide = true
        bottomNav.startAnimation(animation)
    }


    fun showLineScaleLoading(message: String? = null) {
        lineScaleLoader?.lineScaleLoader?.dismiss()
        lineScaleLoader = Loader(this)
        lineScaleLoader?.showMessage(message)
        if (lineScaleLoader?.lineScaleLoader?.isShowing == true) return
        lineScaleLoader?.lineScaleLoader?.show()
    }

    fun hideLineScaleLoading() {
        lineScaleLoader?.lineScaleLoader?.dismiss()
    }


}
