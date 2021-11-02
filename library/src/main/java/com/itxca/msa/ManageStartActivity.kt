package com.itxca.msa

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.savedstate.SavedStateRegistryOwner
import java.util.*
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
    private lateinit var startActivityResultDeque : LinkedBlockingDeque<StartActivityResult>

    /**
     * https://developer.android.google.cn/reference/kotlin/androidx/activity/result/ActivityResultLauncher.html
     * [androidx.activity.result.ActivityResultLauncher]
     */
    private lateinit var activityForResult: ActivityResultLauncher<Intent>

    /**
     * current context
     */
    private var msaContext: Context? = null

    /**
     * current activity result callback
     */
    private val activityResultCallback = object : ActivityResultCallback<ActivityResult> {
        override fun onActivityResult(result: ActivityResult?) {
            result ?: return
            checkInit()
            startActivityResultDeque.pollFirst()?.invoke(result.resultCode, result.data)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun <Host : LifecycleOwner> Host.initManageStartActivity() {
        when (this) {
            is ComponentActivity -> {
                startActivityResultDeque = bindHostSaveState() ?: LinkedBlockingDeque()
                msaContext = this
                activityForResult = registerForActivityResult(
                        ActivityResultContracts.StartActivityForResult(),
                        activityResultCallback
                    )
            }
            is Fragment -> {
                startActivityResultDeque = bindHostSaveState() ?: LinkedBlockingDeque()
                msaContext = requireContext()
                activityForResult = registerForActivityResult(
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
     * save host callback state
     */
    private fun SavedStateRegistryOwner.bindHostSaveState(): LinkedBlockingDeque<StartActivityResult>? {
       val saveStateKey = savedStateRegistry.consumeRestoredStateForKey(SAVE_STATE_KEY)
            ?.getString(SAVE_STATE_BUNDLE_KEY) ?: UUID.randomUUID().toString()

        savedStateRegistry.registerSavedStateProvider(SAVE_STATE_KEY) {
            Bundle().apply {
                if (::startActivityResultDeque.isInitialized) {
                    msaResultSaveState[saveStateKey] = startActivityResultDeque
                    putString(SAVE_STATE_BUNDLE_KEY, saveStateKey)
                }
            }
        }

        return msaResultSaveState.remove(saveStateKey)
    }

    /**
     * check is init
     */
    private fun checkInit(){
        if (!::startActivityResultDeque.isInitialized){
            throw IllegalArgumentException("call `initManageStartActivity` required before `onResume`.")
        }
    }

    /**
     * [msaContext] run on not null
     */
    private fun runSafeContext(block: Context.() -> Unit) = msaContext?.block()

    /**
     * {@inheritDoc}
     */
    override fun startActivityForResult(target: KClass<out Activity>, block: Intent.() -> Unit, result: StartActivityResult) {
        runSafeContext {
            checkInit()
            startActivityResultDeque.offerFirst(result)
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
            checkInit()
            startActivityResultDeque.offerFirst(result)
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

    companion object {
        /**
         * key is [androidx.savedstate.SavedStateRegistry.SavedStateProvider]
         */
        private const val SAVE_STATE_KEY = "mas_save_state"

        /**
         * save state key
         */
        private const val SAVE_STATE_BUNDLE_KEY = "mas_bundle_tag"

        /**
         * result save
         */
        private val msaResultSaveState = LinkedHashMap<String, LinkedBlockingDeque<StartActivityResult>>()
    }
}