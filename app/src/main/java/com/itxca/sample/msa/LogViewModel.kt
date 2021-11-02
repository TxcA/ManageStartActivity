package com.itxca.sample.msa

import android.content.Intent
import androidx.activity.result.ActivityResult
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.sample.msa
 * Create Time  : 2021-11-02 16:24
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/

class LogViewModel : ViewModel() {

    /**
     * 通过 ViewModel+ LiveData 兼容 Activity 在屏幕旋转旋转、语言切换等情况下重建恢复
     */
    val logString = MutableLiveData<ActivityResult>()

    fun printLog(code: Int, data: Intent?) {
        logString.value = ActivityResult(code, data)
    }
}