package com.itxca.sample.msa

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.itxca.sample.msa.databinding.ActivityActivityResultContractsBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


/**
 *
 * Project Name : ManageStartActivity
 * Package Name : com.itxca.sample.msa
 * Create Time  : 2021-10-22 23:31
 * Create By    : @author xIao
 * Version      : 1.0.0
 *
 **/

class ActivityResultContractsActivity : AppCompatActivity() {

    private val viewBinding by lazy { ActivityActivityResultContractsBinding.inflate(layoutInflater) }

    //<editor-fold desc="save uri">
    private val videoSaveUri by lazy { Uri.fromFile(externalCacheDir?.resolve("video.mp4")) }
    private val imageSaveUri by lazy { Uri.fromFile(externalCacheDir?.resolve("image.png")) }
    //</editor-fold>

    //<editor-fold desc="Contracts">
    /**
     * 自定义Contract
     */
    private val launcherContractActivity =
        ContractActivity.Contract().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("result: $it")
            }
        }

    /**
     * 可以理解为 [startActivityForResult]
     */
    private val launcherStartActivityForResult =
        ActivityResultContracts.StartActivityForResult().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog(it.toString())
            }
        }

    /**
     * 通过`MediaStore.ACTION_IMAGE_CAPTURE`拍照并保存
     */
    private val launcherTakePicture =
        ActivityResultContracts.TakePicture().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("saveSuccess: $it")
            }
        }

    /**
     * 通过`MediaStore.ACTION_IMAGE_CAPTURE`拍照
     */
    private val launcherTakePicturePreview =
        ActivityResultContracts.TakePicturePreview().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("bitmap : $it")
            }
        }

    /**
     * 通过`MediaStore.ACTION_VIDEO_CAPTURE`拍摄视频并保存
     * androidx.activity 1.3.0-alpha08后提供，androidx.appcompat好像还没提供该类
     */
    private val launcherCaptureVideo =
        ActivityResultContracts.CaptureVideo().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("saveSuccess: $it")
            }
        }

    /**
     * 请求单个权限
     */
    private val launcherRequestPermission =
        ActivityResultContracts.RequestPermission().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("grant: $it")
            }
        }

    /**
     * 请求多个权限
     */
    private val launcherRequestMultiplePermissions =
        ActivityResultContracts.RequestMultiplePermissions().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog(it.map { result -> "${result.key} - ${result.value}" }
                    .joinToString())
            }
        }

    /**
     * 通过`Intent.ACTION_CREATE_DOCUMENT`创建一个文件
     */
    private val launcherCreateDocument =
        ActivityResultContracts.CreateDocument().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("uri= $it")
            }
        }

    /**
     * 通过`Intent.ACTION_GET_CONTENT`获取一个文件
     * 这个方法可以通过`android.content.ContentResolver.openInputStream`获取到文件的原始数据
     */
    private val launcherGetContent =
        ActivityResultContracts.GetContent().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("uri= $it, size= ${
                    contentResolver.openInputStream(it)?.use { input ->
                        input.readBytes().size
                    } ?: 0
                }")
            }
        }

    /**
     * 通过`Intent.ACTION_GET_CONTENT`及`Intent.EXTRA_ALLOW_MULTIPLE`获取一个或多个文件
     * 这个方法可以通过`android.content.ContentResolver.openInputStream`获取到文件的原始数据
     */
    private val launcherGetMultipleContents =
        ActivityResultContracts.GetMultipleContents().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog(it.mapIndexed { index, uri ->
                    "index= $index, uri: $it, size= ${
                        contentResolver.openInputStream(uri)?.use { input ->
                            input.readBytes().size
                        } ?: 0
                    }"
                }.joinToString("\n"))
            }
        }

    /**
     * 通过`Intent.ACTION_OPEN_DOCUMENT`选择文件
     */
    private val launcherOpenDocument =
        ActivityResultContracts.OpenDocument().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("uri= $it")
            }
        }

    /**
     * 通过`Intent.ACTION_OPEN_DOCUMENT_TREE`选择一个目录，返回一个Uri并得到该目录下全部文档的管理权
     */
    private val launcherOpenDocumentTree =
        ActivityResultContracts.OpenDocumentTree().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("uri= $it")
            }
        }

    /**
     * 通过`Intent.ACTION_OPEN_DOCUMENT`及`Intent.EXTRA_ALLOW_MULTIPLE`获取一个或多个文件
     */
    private val launcherOpenMultipleDocuments =
        ActivityResultContracts.OpenMultipleDocuments().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog(it.mapIndexed { index, uri ->
                    "index= $index, uri: $it, size= ${
                        contentResolver.openInputStream(uri)?.use { input ->
                            input.readBytes().size
                        } ?: 0
                    }"
                }.joinToString("\n"))
            }
        }

    /**
     * 通过`Intent.ACTION_PICK`从系统通讯录中获取联系人
     */
    private val launcherPickContact =
        ActivityResultContracts.PickContact().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("uri= $it")
            }
        }

    /**
     * 通过`MediaStore.ACTION_VIDEO_CAPTURE`拍摄视频并保存
     * 弃用了，官方解释是缩略图的Bitmap的返回不稳定，替换方法为[ActivityResultContracts.CaptureVideo]
     */
    @Deprecated("已被标记为弃用")
    private val launcherTakeVideo =
        ActivityResultContracts.TakeVideo().let { contract ->
            registerForActivityResult(contract) {
                contract.updateLog("thumbnail bitmap= $it")
            }
        }
    //</editor-fold>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
        VmPolicy.Builder().run {
            StrictMode.setVmPolicy(build())
            detectFileUriExposure()
        }
        title = this::class.java.simpleName
        initView()
    }

    private fun initView() {
        /** 该方法会抛出异常，原因为必须在`STARTED`前调用`registerForActivityResult `*/
        viewBinding.btnStartActivityForResultError.setOnClickListener {
            lifecycleScope.launch {
                Toast.makeText(this@ActivityResultContractsActivity, "该方法会抛出`IllegalStateException`异常", Toast.LENGTH_LONG)
                    .show()
                delay(500L)
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                }.launch(Intent(this@ActivityResultContractsActivity, SecondActivity::class.java))
            }
        }

        viewBinding.btnContractActivity.setOnClickListener {
            launcherContractActivity.launch("hi, Activity Result API!")
        }

        viewBinding.btnStartActivityForResult.setOnClickListener {
            launcherStartActivityForResult.launch(Intent(this@ActivityResultContractsActivity, SecondActivity::class.java))
        }

        viewBinding.btnTakePicture.setOnClickListener {
            launcherTakePicture.launch(imageSaveUri)
        }

        viewBinding.btnTakePicturePreview.setOnClickListener {
            launcherTakePicturePreview.launch(null)
        }

        viewBinding.btnCaptureVideo.setOnClickListener {
            launcherCaptureVideo.launch(videoSaveUri)
        }

        viewBinding.btnRequestPermission.setOnClickListener {
            launcherRequestPermission.launch(Manifest.permission.READ_PHONE_STATE)
        }

        viewBinding.btnRequestMultiplePermissions.setOnClickListener {
            launcherRequestMultiplePermissions.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE))
        }

        viewBinding.btnCreateDocument.setOnClickListener {
            launcherCreateDocument.launch("ManageStartActivity.txt")
        }

        viewBinding.btnGetContent.setOnClickListener {
            launcherGetContent.launch(MIME_IMAGE)
        }

        viewBinding.btnGetMultipleContents.setOnClickListener {
            launcherGetMultipleContents.launch(MIME_IMAGE)
        }

        viewBinding.btnOpenDocument.setOnClickListener {
            launcherOpenDocument.launch(arrayOf(MIME_IMAGE))
        }

        viewBinding.btnOpenDocumentTree.setOnClickListener {
            launcherOpenDocumentTree.launch(null)
        }

        viewBinding.btnOpenMultipleDocuments.setOnClickListener {
            launcherOpenMultipleDocuments.launch(arrayOf(MIME_IMAGE))
        }

        viewBinding.btnPickContact.setOnClickListener {
            launcherPickContact.launch(null)
        }

        viewBinding.btnTakeVideo.setOnClickListener {
            launcherTakeVideo.launch(videoSaveUri)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun <T : ActivityResultContract<*, *>> T.updateLog(message: String) {
        viewBinding.tvLogcat.text = "[${this::class.java.simpleName}] $message"
    }

    companion object{
        private const val MIME_IMAGE = "image/*"
    }
}