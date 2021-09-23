package com.itxca.sample.msa.base

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.itxca.msa.IMsa
import com.itxca.msa.msa

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.sample.msa.base
 * Create Time  : 2021-09-23 14:42
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/


abstract class BaseFragment : Fragment(), IMsa by msa()  {
    override fun onCreate(savedInstanceState: Bundle?) {
        initManageStartActivity(this)
        super.onCreate(savedInstanceState)
    }
}