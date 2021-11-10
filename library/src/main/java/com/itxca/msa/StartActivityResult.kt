package com.itxca.msa

import android.content.Intent
import androidx.annotation.Keep

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.msa
 * Create Time  : 2021-09-23 14:36
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/

typealias StartActivityResult = (
    /**
     * Activity result code
     */
    code: Int,
    /**
     * Activity result intent
     */
    data: Intent?,
) -> Unit

@Keep
data class Result(
    /**
     * Activity result code
     */
    val resultCode: Int,
    /**
     * Activity result intent
     */
    val data: Intent?,
)