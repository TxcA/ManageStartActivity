package com.itxca.msa

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
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
     * @receiver lifecycleHost `ComponentActivity` or `Fragment`
     */
    fun <Host : LifecycleOwner> Host.initManageStartActivity()

    /**
     * Start activity for result
     * @param target Activity
     * @param block intent
     * @param options [androidx.core.app.ActivityOptionsCompat]
     * @param result [com.itxca.msa.StartActivityResult]
     */
    fun startActivityForResult(target: KClass<out Activity>, block: Intent.() -> Unit = {}, options: () -> ActivityOptionsCompat? = { null }, result: StartActivityResult)
    fun <T : KClass<out Activity>> T.startForResult(block: Intent.() -> Unit = {}, options: () -> ActivityOptionsCompat? = { null }, result: StartActivityResult)

    /**
     * Start activity for result
     * @param intent Intent
     * @param options [androidx.core.app.ActivityOptionsCompat]
     * @param result [com.itxca.msa.StartActivityResult]
     */
    fun startActivityForResult(intent: Intent, options: () -> ActivityOptionsCompat? = { null }, result: StartActivityResult)
    fun Intent.startForResult(options: () -> ActivityOptionsCompat? = { null }, result: StartActivityResult)

    /**
     * Start activity for result sync (Coroutine)
     * @param target Activity
     * @param options [androidx.core.app.ActivityOptionsCompat]
     * @param block intent
     * @return [com.itxca.msa.Result]
     */
    suspend fun startActivityForResultSync(target: KClass<out Activity>, options: () -> ActivityOptionsCompat? = { null }, block: Intent.() -> Unit = {}): Result
    suspend fun <T : KClass<out Activity>> T.startForResultSync(options: () -> ActivityOptionsCompat? = { null }, block: Intent.() -> Unit = {}): Result

    /**
     * Start activity for result sync (Coroutine)
     * @param intent Intent
     * @param options [androidx.core.app.ActivityOptionsCompat]
     * @return [com.itxca.msa.Result]
     */
    suspend fun startActivityForResultSync(intent: Intent, options: () -> ActivityOptionsCompat? = { null }): Result
    suspend fun Intent.startForResultSync(options: () -> ActivityOptionsCompat? = { null }): Result

    /**
     * Start activity
     * @param target Activity
     * @param options [androidx.core.app.ActivityOptionsCompat]
     * @param block intent
     */
    fun startActivity(target: KClass<out Activity>, options: () -> ActivityOptionsCompat? = { null }, block: Intent.() -> Unit = {})
    fun <T : KClass<out Activity>> T.start(options: () -> ActivityOptionsCompat? = { null }, block: Intent.() -> Unit = {})
    fun Intent.start(options: () -> ActivityOptionsCompat? = { null })
}