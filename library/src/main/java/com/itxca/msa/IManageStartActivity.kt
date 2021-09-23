package com.itxca.msa

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.LifecycleOwner
import kotlin.reflect.KClass

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.msa
 * Create Time  : 2021-09-23 14:36
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/

interface IManageStartActivity {
    /**
     * Initialized host
     * @param lifecycleHost `ComponentActivity` or `Fragment`
     */
    fun <Host : LifecycleOwner> initManageStartActivity(lifecycleHost: Host)

    /**
     * Start activity for result
     * @param target Activity
     * @param block intent
     * @param result [com.itxca.msa.StartActivityResult]
     */
    fun startActivityForResult(target: KClass<out Activity>, block: Intent.() -> Unit = {}, result: StartActivityResult)
    fun <T : KClass<out Activity>> T.startForResult(block: Intent.() -> Unit = {}, result: StartActivityResult)

    /**
     * Start activity for result
     * @param intent Intent
     * @param result [com.itxca.msa.StartActivityResult]
     */
    fun startActivityForResult(intent: Intent, result: StartActivityResult)
    fun Intent.startForResult(result: StartActivityResult)

    /**
     * Start activity for result sync (Coroutine)
     * @param target Activity
     * @param block intent
     * @return [com.itxca.msa.Result]
     */
    suspend fun startActivityForResultSync(target: KClass<out Activity>, block: Intent.() -> Unit = {}): Result
    suspend fun <T : KClass<out Activity>> T.startForResultSync( block: Intent.() -> Unit = {}): Result

    /**
     * Start activity for result sync (Coroutine)
     * @param intent Intent
     * @return [com.itxca.msa.Result]
     */
    suspend fun startActivityForResultSync(intent: Intent): Result
    suspend fun Intent.startForResultSync(): Result

    /**
     * Start activity
     */
    fun startActivity(target: KClass<out Activity>, block: Intent.() -> Unit = {})
    fun <T : KClass<out Activity>> T.start(block: Intent.() -> Unit = {})
    fun Intent.start()
}