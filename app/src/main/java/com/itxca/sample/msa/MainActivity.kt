package com.itxca.sample.msa

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.itxca.sample.msa.base.BaseActivity
import com.itxca.sample.msa.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

@SuppressLint("SetTextI18n")
class MainActivity : BaseActivity() {
    private val viewBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: LogViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.startActivityTarget.setOnClickListener(::startActivityTarget)
        viewBinding.startActivityForResultTarget.setOnClickListener(::startActivityForResultTarget)
        viewBinding.startActivityForResultIntent.setOnClickListener(::startActivityForResultIntent)
        viewBinding.startActivityForResultSyncTarget.setOnClickListener(::startActivityForResultSyncTarget)
        viewBinding.startActivityForResultSyncIntent.setOnClickListener(::startActivityForResultSyncIntent)
        viewBinding.startTestFragment.setOnClickListener(::startTestFragment)
        viewBinding.startActivityResultContracts.setOnClickListener {
            startActivity(ActivityResultContractsActivity::class)
        }

        viewModel.logString.observe(this) { result ->
            result ?: return@observe
            showCodeData(result.resultCode, result.data)
        }
    }

    private fun startActivityTarget(v: View) {
        startActivity(SecondActivity::class) {
            putExtra(SecondActivity.EXTRA_DATA, this@MainActivity.extraMessage())
        }
    }

    private fun startActivityForResultTarget(v: View) {
        viewBinding.tvLogcat.text = ""
        startActivityForResult(SecondActivity::class, {
            putExtra(SecondActivity.EXTRA_DATA, this@MainActivity.extraMessage())
        }) { code, data ->
            viewModel.printLog(code, data)
        }
    }

    private fun startActivityForResultIntent(v: View) {
        viewBinding.tvLogcat.text = ""
        startActivityForResult(SecondActivity.buildIntent(this, extraMessage())) { code, data ->
            viewModel.printLog(code, data)
        }
    }

    private fun startActivityForResultSyncTarget(v: View) {
        lifecycleScope.launch {
            val (code, data) = startActivityForResultSync(SecondActivity::class) {
                putExtra(SecondActivity.EXTRA_DATA, this@MainActivity.extraMessage())
            }
            viewModel.printLog(code, data)
        }
    }

    private fun startActivityForResultSyncIntent(v: View) {
        lifecycleScope.launch {
            val (code, data) = startActivityForResultSync(
                SecondActivity.buildIntent(this@MainActivity, extraMessage())
            )
            viewModel.printLog(code, data)
        }
    }

    private fun startTestFragment(v: View) {
        startActivityForResult(FragmentActivity::class) { code, data ->
            viewModel.printLog(code, data)
        }
    }

    private fun showCodeData(code: Int, data: Intent?) {
        viewBinding.tvLogcat.text = """
                code: $code
                data: ${data?.getStringExtra(SecondActivity.EXTRA_DATA)}
            """.trimIndent()
    }
}