package com.itxca.sample.msa

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.app.ActivityOptionsCompat
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
        startActivity(SecondActivity::class, { v.randomOptionsCompat }) {
            putExtra(SecondActivity.EXTRA_DATA, this@MainActivity.extraMessage())
        }
    }

    private fun startActivityForResultTarget(v: View) {
        viewBinding.tvLogcat.text = ""
        startActivityForResult(SecondActivity::class, {
            putExtra(SecondActivity.EXTRA_DATA, this@MainActivity.extraMessage())
        }, { v.randomOptionsCompat }) { code, data ->
            viewModel.printLog(code, data)
        }
    }

    private fun startActivityForResultIntent(v: View) {
        viewBinding.tvLogcat.text = ""
        startActivityForResult(SecondActivity.buildIntent(this, extraMessage()),
            { v.randomOptionsCompat }) { code, data ->
            viewModel.printLog(code, data)
        }
    }

    private fun startActivityForResultSyncTarget(v: View) {
        viewBinding.tvLogcat.text = ""
        lifecycleScope.launch {
            val (code, data) = startActivityForResultSync(SecondActivity::class,
                { v.randomOptionsCompat }) {
                putExtra(SecondActivity.EXTRA_DATA, this@MainActivity.extraMessage())
            }
            viewModel.printLog(code, data)
        }
    }

    private fun startActivityForResultSyncIntent(v: View) {
        viewBinding.tvLogcat.text = ""
        lifecycleScope.launch {
            val (code, data) = startActivityForResultSync(
                SecondActivity.buildIntent(this@MainActivity, extraMessage())
            ){ v.randomOptionsCompat }
            viewModel.printLog(code, data)
        }
    }

    private fun startTestFragment(v: View) {
        viewBinding.tvLogcat.text = ""
        startActivityForResult(FragmentActivity::class,
            options = { v.randomOptionsCompat }) { code, data ->
            viewModel.printLog(code, data)
        }
    }

    private fun showCodeData(code: Int, data: Intent?) {
        viewBinding.tvLogcat.text = """
                code: $code
                data: ${data?.getStringExtra(SecondActivity.EXTRA_DATA)}
            """.trimIndent()
    }

    private fun String.toast() {
        Toast.makeText(this@MainActivity, this, Toast.LENGTH_SHORT).show()
    }

    private val View.randomOptionsCompat: ActivityOptionsCompat?
        get() = when ((0..6).random()) {
            0 -> {
                "Custom animation".toast()
                ActivityOptionsCompat.makeCustomAnimation(this@MainActivity, R.anim.bottom_in, R.anim.bottom_out)
            }
            1 -> {
                "ScaleUp animation".toast()
                ActivityOptionsCompat.makeScaleUpAnimation(this, height / 2, width / 2, 0, 0)
            }
            2 -> {
                "ClipReveal animation".toast()
                ActivityOptionsCompat.makeClipRevealAnimation(this, height / 2, width / 2, 0, 0)
            }
            3 -> {
                "ThumbnailScaleUp animation".toast()
                ActivityOptionsCompat.makeThumbnailScaleUpAnimation(this, BitmapFactory.decodeResource(resources,R.mipmap.logo), height / 2, width / 2)
            }
            else -> {
                "No animation".toast()
                null
            }
        }
}