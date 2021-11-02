package com.itxca.sample.msa

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.itxca.msa.IMsa
import com.itxca.msa.msa
import kotlinx.coroutines.launch

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.sample.msa
 * Create Time  : 2021-09-23 21:29
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/

// 注意实现 `IMsa by msa()`
@Suppress("unused")
class SampleActivity : AppCompatActivity(), IMsa by msa() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 从1.0.4开始, 必须在super.onCreate()后初始化 initMangeStartActivity
        // 因为需要使用 SavedStateRegistry 来保存回调状态
        initManageStartActivity()
    }

    /**
     * 直接启动Activity
     * 3 种方式
     */
    fun startActivity() {
        // Android习惯模式，传入KClass即可
        startActivity(MainActivity::class) {
            putExtra("key", "value")
        }

        // KClass扩展方法模式
        MainActivity::class.start {
            putExtra("key", "value")
        }

        // Intent扩展方法模式
        Intent(this, MainActivity::class.java).apply {
            putExtra("key", "value")
        }.start()
    }

    /**
     * 启动Activity并需要回调结果
     * 4 种方法
     */
    fun startActivityForResult() {
        // Android习惯模式，传入KClass即可
        startActivityForResult(MainActivity::class, {
            putExtra("key", "value")
        }) { code: Int, data: Intent? ->
            // code = resultCode
        }

        // KClass扩展方法模式
        MainActivity::class.startForResult({
            putExtra("key", "value")
        }) {code: Int, data: Intent? ->
            // code = resultCode
        }

        // Android习惯模式，传入Intent即可
        startActivityForResult(Intent(this, MainActivity::class.java).apply {
            putExtra("key", "value")
        }){ code: Int, data: Intent? ->
            // code = resultCode
        }

        // Intent扩展方法模式
        Intent(this, MainActivity::class.java).apply {
            putExtra("key", "value")
        }.startForResult { code: Int, data: Intent? ->
            // code = resultCode
        }
    }

    /**
     * Kotlin协程挂起函数
     * 4 种方式
     */
    fun startActivityForResultCoroutine() {
        lifecycleScope.launch {
            // Android习惯模式，传入KClass即可
           val (code1: Int, data1: Intent?) = startActivityForResultSync(MainActivity::class) {
               putExtra("key", "value")
           }

            // KClass扩展方法模式
            val (code2: Int, data2: Intent?) = MainActivity::class.startForResultSync {
                putExtra("key", "value")
            }

            // Android习惯模式，传入Intent即可
            val (code3: Int, data3: Intent?) = startActivityForResultSync(Intent(this@SampleActivity, MainActivity::class.java).apply {
                putExtra("key", "value")
            })

            // Intent扩展方法模式
            val (code4: Int, data4: Intent?) = Intent(this@SampleActivity, MainActivity::class.java).apply {
                putExtra("key", "value")
            }.startForResultSync()
        }
    }
}