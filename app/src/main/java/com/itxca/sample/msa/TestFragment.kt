package com.itxca.sample.msa

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.itxca.sample.msa.base.BaseFragment
import com.itxca.sample.msa.databinding.FragmentTestBinding

/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.sample.msa
 * Create Time  : 2021-09-23 16:12
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/
@SuppressLint("SetTextI18n")
class TestFragment : BaseFragment() {
    private lateinit var viewBinding: FragmentTestBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentTestBinding.inflate(inflater, container, false)
        .also { vb -> viewBinding = vb }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.tvFlag.text = arguments?.getString(FLAG_DATA) ?: "null"

        viewBinding.finish.setOnClickListener(::finish)
        viewBinding.finishResultOk.setOnClickListener(::finishResultOk)
        viewBinding.startActivityForResultIntent.setOnClickListener(::startActivityForResultIntent)
    }

    private fun finish(v: View) {
        requireActivity().finish()
    }

    private fun finishResultOk(v: View) {
        requireActivity().setResult(AppCompatActivity.RESULT_OK, Intent().apply {
            putExtra(SecondActivity.EXTRA_DATA, this@TestFragment.extraMessage())
        })
        requireActivity().finish()
    }

    private fun startActivityForResultIntent(v: View) {
        viewBinding.tvLogcat.text = ""
        startActivityForResult(SecondActivity.buildIntent(requireContext(), extraMessage()))
        { code, data ->
            viewBinding.tvLogcat.text = """
                code: $code
                data: ${data?.getStringExtra(SecondActivity.EXTRA_DATA)}
            """.trimIndent()
        }
    }

    companion object {
        private const val FLAG_DATA = "flag_data"

        fun newInstance(flag: String): TestFragment =
            TestFragment().also { fm ->
                fm.arguments = Bundle().apply { putString(FLAG_DATA, flag) }
            }
    }
}