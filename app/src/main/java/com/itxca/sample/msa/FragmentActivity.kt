package com.itxca.sample.msa

import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import com.itxca.sample.msa.base.BaseActivity
import com.itxca.sample.msa.databinding.ActivityFragmentBinding

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.sample.msa
 * Create Time  : 2021-09-23 16:06
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/

class FragmentActivity : BaseActivity() {
    private val viewBinding by lazy { ActivityFragmentBinding.inflate(layoutInflater) }

    private val titles = listOf("1", "2")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        title = this::class.java.simpleName

        viewBinding.viewPager.adapter = FragmentAdapter(
            this, listOf(
                TestFragment.newInstance(titles[0]),
                TestFragment.newInstance(titles[1])
            )
        )
        TabLayoutMediator(viewBinding.tab, viewBinding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()
    }
}