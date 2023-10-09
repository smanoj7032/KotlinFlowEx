package com.example.koltinflowex.presentation.common.base

import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.IdRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.koltinflowex.R
import com.example.koltinflowex.common.network.connectionhelper.NetworkChangeCallback
import com.example.koltinflowex.common.network.connectionhelper.NetworkChangeReceiver
import com.example.koltinflowex.presentation.common.compooundviews.ErrorView
import com.example.koltinflowex.presentation.common.hideKeyBoard
import com.example.koltinflowex.presentation.common.isNetworkAvailable

abstract class BaseFragment<Binding : ViewDataBinding> : Fragment(), NetworkChangeCallback {
    lateinit var baseContext: Context
    lateinit var mainbinding: Binding
    open val onRetry: (() -> Unit)? = null
    private var networkChangeReceiver: NetworkChangeReceiver? = null
    val parentActivity: BaseActivity<*>?
        get() = activity as? BaseActivity<*>
    private val viewModel: BaseViewModel by lazy {
        ViewModelProvider(this)[BaseViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onCreateView(view, savedInstanceState)
        setObserver()
        initViews()
        view.findViewById<ErrorView>(R.id.error_view)?.onRetry = onRetry
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        networkChangeReceiver = NetworkChangeReceiver(this)
        parentActivity?.registerReceiver(
            networkChangeReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val layout: Int = getLayoutResource()
        mainbinding = DataBindingUtil.inflate(layoutInflater, layout, container, false)
        return mainbinding.root
    }

    override fun onPause() {
        super.onPause()
        activity?.hideKeyBoard()
    }

    override fun onDestroy() {
        super.onDestroy()
        parentActivity?.unregisterReceiver(networkChangeReceiver)
    }

    protected abstract fun onCreateView(view: View, saveInstanceState: Bundle?)
    protected abstract fun setObserver()
    protected abstract fun initViews()
    protected abstract fun getLayoutResource(): Int

    fun onLoading(show: Boolean) {
        val progressBar: View? = view?.findViewById(R.id.progress_bar)
        progressBar?.visibility = if (show) View.VISIBLE else View.GONE
        val errorView: ErrorView? = view?.findViewById(R.id.error_view)
        errorView?.visibility = if (show) View.GONE else View.VISIBLE
        errorView?.onRetry = onRetry
    }

    fun onError(error: Throwable, showErrorView: Boolean) {
        parentActivity?.onError(error, showErrorView)
    }

    fun View.navigateWithId(id: Int, @IdRes currentDestinationId: Int?, bundle: Bundle? = null) =
        try {
            activity?.hideKeyBoard()
            this.findNavController().navigate(id, bundle)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    fun View.navigateBack() = try {
        activity?.hideKeyBoard()
        this.findNavController().navigateUp()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    override fun onNetworkChanged(status: Boolean?) {}
}