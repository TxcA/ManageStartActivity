package com.itxca.sample.msa

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import com.itxca.sample.msa.databinding.ActivitySecondBinding

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.sample.msa
 * Create Time  : 2021-10-23 3:16
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/

class ContractActivity : AppCompatActivity() {

    private val viewBinding by lazy {
        ActivitySecondBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        title = this::class.java.simpleName

        viewBinding.finish.setOnClickListener(::finish)
        viewBinding.finishResultOk.setOnClickListener(::finishResultOk)

        loadIntentData()
    }

    private fun loadIntentData() {
        viewBinding.tvLogcat.text = intent?.getStringExtra(Contract.EXTRA_NAME) ?: "error"
    }

    private fun finish(v: View) {
        finish()
    }

    private fun finishResultOk(v: View) {
        setResult(RESULT_OK, Intent().apply {
            putExtra(Contract.EXTRA_RESULT, this@ContractActivity.extraMessage())
        })
        finish()
    }

    /**
     * 继承[ActivityResultContract]
     *
     * 泛型第一个参数为传入到 [ContractActivity] 的参数
     * 第二个参数为 [ContractActivity] 返回给启动Activity的返回值
     */
    class Contract : ActivityResultContract<String, String>() {
        /**
         * 创建启动Intent
         * @param context [Context]
         * @param input 当前类的第一个泛型参, 这里自己实现传递过程
         */
        override fun createIntent(context: Context, input: String): Intent =
            Intent(context, ContractActivity::class.java).apply {
                putExtra(EXTRA_NAME, "$input - ${System.currentTimeMillis()}")
            }

        /**
         * 解析结果
         * @param resultCode [Activity.setResult] 的 resultCode
         * @param intent [Activity.setResult] 的 intent
         * 其实这里类似 [Activity.onActivityResult] 处理过程，只是由于返回是明确的，所以少了requestCode
         */
        override fun parseResult(resultCode: Int, intent: Intent?): String {
            return if (resultCode == Activity.RESULT_OK && intent != null) {
                "Contract - ${intent.getStringExtra(EXTRA_RESULT)}"
            } else {
                "error"
            }
        }

        companion object {
            /** EXTRA */
            const val EXTRA_NAME = "extra_name"
            const val EXTRA_RESULT = "extra_result"
        }
    }
}