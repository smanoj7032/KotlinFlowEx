package com.example.koltinflowex.presentation.common.multifab

import android.animation.ObjectAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.FrameLayout
import android.widget.ImageButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.koltinflowex.R
import com.example.koltinflowex.presentation.common.base.DoubleClickListener

class MultiFloatingActionButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), MiniFabAdapter.OnFabItemClickListener,
    View.OnClickListener {

    private var miniFabRecyclerView: RecyclerView
    private var mainFab: ImageButton
    private var fabItemClickListener: MiniFabAdapter.OnFabItemClickListener? = null
    private val fadeInAnimation = AlphaAnimation(0f, 3f).apply { duration = 1000 }
    private val fadeOutAnimation = AlphaAnimation(3f, 0f).apply { duration = 500 }
    private var isRotated = false

    init {
        LayoutInflater.from(context).inflate(R.layout.fab_item, this, true)
        miniFabRecyclerView = findViewById(R.id.miniFabRecyclerView)
        mainFab = findViewById(R.id.mainFab)

        mainFab.setOnClickListener(DoubleClickListener(context) {
            toggleMiniFabItemsVisibility()
        })


        miniFabRecyclerView.adapter = MiniFabAdapter(this)
        miniFabRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private val rotateAnimator: ObjectAnimator =
        ObjectAnimator.ofFloat(mainFab, View.ROTATION, 0f, 45f).apply {
            duration = 300
            interpolator = android.view.animation.AccelerateDecelerateInterpolator()
        }

    private fun toggleMiniFabItemsVisibility() {
        if (miniFabRecyclerView.visibility == View.VISIBLE) {
            rotateButton()
            miniFabRecyclerView.startAnimation(fadeOutAnimation)
            miniFabRecyclerView.visibility = View.GONE
        } else {
            rotateButton()
            miniFabRecyclerView.startAnimation(fadeInAnimation)
            miniFabRecyclerView.visibility = View.VISIBLE
        }
    }

    private fun rotateButton() {
        if (!rotateAnimator.isRunning) {
            if (isRotated) {
                rotateAnimator.setFloatValues(135f, 0f)
            } else {
                rotateAnimator.setFloatValues(0f, 135f)
            }
            rotateAnimator.start()
            isRotated = !isRotated
        }
    }

    // Public method to set the mini fab items
    fun setMiniFabItems(items: List<MultiFabItem>) {
        (miniFabRecyclerView.adapter as? MiniFabAdapter)?.setItems(items)
    }

    fun setOnFabItemClickListener(listener: MiniFabAdapter.OnFabItemClickListener?) {
        fabItemClickListener = listener
    }

    override fun onFabItemClick(item: MultiFabItem) {
        rotateButton()
        miniFabRecyclerView.startAnimation(fadeOutAnimation)
        miniFabRecyclerView.visibility = View.GONE
        fabItemClickListener?.onFabItemClick(item)
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {

        }
    }
}