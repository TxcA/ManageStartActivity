package com.itxca.sample.msa

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.itxca.sample.msa.base.BaseActivity
import com.itxca.sample.msa.databinding.ActivitySecondBinding

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.sample.msa
 * Create Time  : 2021-09-23 14:56
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/

class SecondActivity : BaseActivity() {
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
        intent?.getStringExtra(EXTRA_DATA)?.let {
            viewBinding.tvLogcat.text = it
        }
    }

    private fun finish(v: View) {
        finish()
    }

    private fun finishResultOk(v: View) {
        setResult(RESULT_OK, Intent().apply {
            putExtra(EXTRA_DATA, this@SecondActivity.extraMessage())
        })
        finish()
    }

    companion object {
        const val EXTRA_DATA = "extra_data"

        fun buildIntent(context: Context, data: String): Intent =
            Intent(context, SecondActivity::class.java).also { intent ->
                intent.putExtra(EXTRA_DATA, data)
            }
    }
}