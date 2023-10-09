package com.example.koltinflowex.presentation.views.fragment.demo

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class PlayerFragmentAdapter(
    fragment: FragmentActivity,
    val list: ArrayList<String>
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return SettingFragment.newInstance(list[position])
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
}