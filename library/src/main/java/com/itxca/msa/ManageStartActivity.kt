package com.itxca.msa

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.LinkedBlockingDeque
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
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

class ManageStartActivity : IManageStartActivity {
    /**
     * start activity for result deque
     */
    private val startActivityResultDeque = LinkedBlockingDeque<StartActivityResult>()

    /**
     * https://developer.android.google.cn/reference/kotlin/androidx/activity/result/ActivityResultLauncher.html
     * [androidx.activity.result.ActivityResultLauncher]
     */
    private lateinit var activityForResult: ActivityResultLauncher<Intent>

    /**
     * current context
     */
    private var context: Context? = null

    /**
     * current activity result callback
     */
    private val activityResultCallback = object : ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult?) {
            result ?: return
            startActivityResultDeque.pop()?.invoke(result.resultCode, result.data)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun <Host : LifecycleOwner> initManageStartActivity(lifecycleHost: Host) {
        when (lifecycleHost) {
            is ComponentActivity -> {
                context = lifecycleHost
                activityForResult =
                    lifecycleHost.registerForActivityResult(
                        ActivityResultContracts.StartActivityForResult(),
                        activityResultCallback
                    )
            }
            is Fragment -> {
                context = lifecycleHost.requireContext()
                activityForResult =
                    lifecycleHost.registerForActivityResult(
                        ActivityResultContracts.StartActivityForResult(),
                        activityResultCallback
                    )
            }
            else -> {
                throw IllegalArgumentException("IManageStartActivity only support `ComponentActivity` and `Fragment`.")
            }
        }
    }

    /**
     * [context] run on not null
     */
    private fun runSafeContext(block: Context.() -> Unit) = context?.block()

    /**
     * {@inheritDoc}
     */
    override fun startActivityForResult(target: KClass<out Activity>, block: Intent.() -> Unit, result: StartActivityResult) {
        runSafeContext {
            startActivityResultDeque.push(result)
            activityForResult.launch(Intent(this, target.java).apply(block))
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun <T : KClass<out Activity>> T.startForResult(block: Intent.() -> Unit, result: StartActivityResult) {
        startActivityForResult(this,block,result)
    }

    /**
     * {@inheritDoc}
     */
    override fun startActivityForResult(intent: Intent, result: StartActivityResult) {
        runSafeContext {
            startActivityResultDeque.push(result)
            activityForResult.launch(intent)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun Intent.startForResult(result: StartActivityResult) {
        startActivityForResult(this,result)
    }

    /**
     * {@inheritDoc}
     */
    override suspend fun startActivityForResultSync(target: KClass<out Activity>, block: Intent.() -> Unit): Result = suspendCoroutine {
        startActivityForResult(target, block) { code, intent ->
            it.resume(Result(code, intent))
        }
    }

    /**
     * {@inheritDoc}
     */
    override suspend fun <T : KClass<out Activity>> T.startForResultSync(block: Intent.() -> Unit): Result
        = startActivityForResultSync(this,block)

    /**
     * {@inheritDoc}
     */
    override suspend fun startActivityForResultSync(intent: Intent): Result = suspendCoroutine {
        startActivityForResult(intent) { code, intent ->
            it.resume(Result(code, intent))
        }
    }

    /**
     * {@inheritDoc}
     */
    override suspend fun Intent.startForResultSync(): Result
        = startActivityForResultSync(this)

    /**
     * {@inheritDoc}
     */
    override fun startActivity(target: KClass<out Activity>, block: Intent.() -> Unit) {
        runSafeContext {
            startActivity(Intent(this, target.java).apply(block))
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun <T : KClass<out Activity>> T.start(block: Intent.() -> Unit) {
        startActivity(this,block)
    }

    /**
     * {@inheritDoc}
     */
    override fun Intent.start() {
        runSafeContext {
            this.startActivity(this@start)
        }
    }
}