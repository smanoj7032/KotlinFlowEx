package com.example.koltinflowex.presentation.common.compooundviews

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.example.koltinflowex.databinding.ErrorViewBinding
import com.example.koltinflowex.presentation.common.base.DoubleClickListener

class ErrorView(context: Context, attributeSet: AttributeSet?) :
    FrameLayout(context, attributeSet) {
    var onRetry: (() -> Unit?)? = null

    init {
        var bindingImpl = ErrorViewBinding.inflate(LayoutInflater.from(context), this, false)
        bindingImpl.imageViewRefresh.setOnClickListener(DoubleClickListener {
            onRetry?.invoke()
        })
    }
}