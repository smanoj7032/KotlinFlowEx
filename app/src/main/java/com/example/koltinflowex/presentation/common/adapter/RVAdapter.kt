package com.example.koltinflowex.presentation.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

open class RVAdapter<M, B : ViewDataBinding>(
    private val layoutResId: Int,
    private val modelVarId: Int,

) : RecyclerView.Adapter<RVAdapter.Holder<B>>() {

    private val dataList: MutableList<M> = ArrayList()

    class Holder<S : ViewDataBinding>(var binding: S) : RecyclerView.ViewHolder(binding.root)

    fun addData(data: M) {
        val positionStart = dataList.size
        dataList.add(data)
        notifyItemInserted(positionStart)
    }

    fun clearList() {
        dataList.clear()
        notifyDataSetChanged()
    }

    fun addToList(newDataList: List<M>?) {
        var newDataList = newDataList
        if (newDataList == null) {
            newDataList = emptyList()
        }
        val positionStart = dataList.size
        val itemCount = newDataList.size
        dataList.addAll(newDataList)
        notifyItemRangeInserted(positionStart, itemCount)
    }

    var list: List<M>?
        get() = dataList
        set(newDataList) {
            dataList.clear()
            if (newDataList != null)
                dataList.addAll(newDataList)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder<B> {
        val binding: B =
            DataBindingUtil.inflate(LayoutInflater.from(parent.context), layoutResId, parent, false)
        return Holder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder<B>, position: Int) {
        onBind(holder.binding, dataList[position], position)
        holder.binding.executePendingBindings()
    }

    open fun onBind(binding: B, bean: M, position: Int) {
        binding.setVariable(modelVarId, bean)
    }
}