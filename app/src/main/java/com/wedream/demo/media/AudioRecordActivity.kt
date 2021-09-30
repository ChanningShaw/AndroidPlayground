package com.wedream.demo.media

import android.content.Intent
import android.os.Bundle
import android.view.Choreographer
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import com.wedream.demo.R
import com.wedream.demo.app.DeviceParams
import com.wedream.demo.app.DisposableActivity
import com.wedream.demo.app.PermissionHelper
import com.wedream.demo.render.WaveView
import com.wedream.demo.util.LogUtils.log

class AudioRecordActivity : DisposableActivity() {

    private var recordStatus = 0

    private lateinit var recordButton: Button
    private lateinit var horizontalScrollView: HorizontalScrollView
    private lateinit var container: FrameLayout

    private var recordView: WaveView? = null

    private var frameCallback = Choreographer.FrameCallback {
        if (recordStatus == 1) {
            sendFrameCallback()
        }
        simpleOnce()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_record)
        recordButton = findViewById(R.id.record_button)
        horizontalScrollView = findViewById(R.id.horizontal_scrollView)
        container = findViewById(R.id.record_container)
        recordButton.setOnClickListener {
            recordStatus = ++recordStatus % 3
            toggleRecord()
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        log { "onNewIntent" }
    }

    private fun toggleRecord() {
        if (recordStatus == 1) {
            PermissionHelper.requestPermission(this, object : PermissionHelper.PermissionGrant {
                override fun onPermissionGranted() {
                    startRecord()
                }
            })
        } else if (recordStatus == 2) {
            // 暂停录音
            recordButton.text = "停止录音"
            AudioRecordManager.instance?.stopRecording()
        } else {
            container.removeView(recordView)
            recordButton.text = "开始录音"
        }
    }

    private fun simpleOnce() {
        recordView?.layoutParams?.let {
            it.width = it.width + 1
            recordView?.layoutParams = it
        }
        horizontalScrollView.scrollBy(1, 0)
        AudioRecordManager.instance?.amplitudeData?.let {
            recordView?.setData(it.toTypedArray())
        }
    }

    private fun sendFrameCallback() {
        Choreographer.getInstance().removeFrameCallback(frameCallback)
        Choreographer.getInstance().postFrameCallback(frameCallback)
    }

    private fun startRecord() {
        // 开始录音
        recordView = WaveView(this@AudioRecordActivity)
        recordView?.setBackgroundResource(R.color.color_blue)
        sendFrameCallback()
        val params = FrameLayout.LayoutParams(0, FrameLayout.LayoutParams.MATCH_PARENT)
        params.marginStart = DeviceParams.SCREEN_WIDTH / 2
        params.marginEnd = DeviceParams.SCREEN_WIDTH / 2
        container.addView(recordView, params)
        recordButton.text = "暂停录音"
        AudioRecordManager.instance?.startRecording("", 16, 1)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        log { "onRequestPermissionsResult" }
        if (grantResults.isNotEmpty()) {
            startRecord()
        }
    }
}