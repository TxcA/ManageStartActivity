package com.itxca.sample.msa.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.itxca.msa.IMsa
import com.itxca.msa.msa

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.sample.msa.base
 * Create Time  : 2021-09-23 14:40
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/

abstract class BaseActivity : AppCompatActivity(), IMsa by msa() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initManageStartActivity()
    }
}