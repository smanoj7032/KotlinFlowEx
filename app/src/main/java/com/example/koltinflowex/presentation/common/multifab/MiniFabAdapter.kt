package com.example.koltinflowex.presentation.common.multifab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.koltinflowex.R

class MiniFabAdapter(private val listener: OnFabItemClickListener) :
    RecyclerView.Adapter<MiniFabAdapter.MiniFabViewHolder>()
{

    private val items: MutableList<MultiFabItem> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MiniFabViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemView = inflater.inflate(R.layout.mini_fab_item, parent, false)
        return MiniFabViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MiniFabViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun setItems(newItems: List<MultiFabItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class MiniFabViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val miniFab: ImageButton = itemView.findViewById(R.id.miniFab)

        fun bind(item: MultiFabItem) {
            miniFab.setImageResource(item.iconRes)
            miniFab.setOnClickListener {
                listener.onFabItemClick(item)
            }
        }
    }

    interface OnFabItemClickListener {
        fun onFabItemClick(item: MultiFabItem)
    }
}