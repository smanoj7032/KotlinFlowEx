package com.example.koltinflowex.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView


abstract class RVAdapterWithPaging<M : Any, B : ViewDataBinding>(
    diffCallback: DiffUtil.ItemCallback<M>,
    private val layoutResId: Int,
    private val modelVarId: Int
) : PagingDataAdapter<M, RVAdapterWithPaging.Holder<B>>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<B> {
        val binding: B = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            layoutResId,
            parent,
            false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder<B>, position: Int) {
        getItem(position)?.let { item ->
            onBind(holder.binding, item, position)
            holder.binding.executePendingBindings()
        }
    }

    open fun onBind(binding: B, item: M, position: Int) {
        binding.setVariable(modelVarId, item)
    }

    class Holder<S : ViewDataBinding>(val binding: S) : RecyclerView.ViewHolder(binding.root)

}
