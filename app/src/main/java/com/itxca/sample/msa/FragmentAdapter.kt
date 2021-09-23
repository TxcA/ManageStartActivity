package com.itxca.sample.msa

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.sample.msa
 * Create Time  : 2021-09-23 16:12
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/

class FragmentAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = fragments.size
    override fun createFragment(position: Int): Fragment = fragments[position]
}