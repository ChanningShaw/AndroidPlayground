package com.wedream.demo.media

import android.media.MediaRecorder
import com.wedream.demo.util.LogUtils.printAndDie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import java.util.*
import java.util.concurrent.TimeUnit

class AudioRecordManager private constructor() {
    private var mRecorder: MediaRecorder? = null
    var path = ""
        private set
    private var isRecording = false
    private val mAmplitudeData = ArrayList<Int>()
    private var mSimpleInterval = LISTENER_INTERVAL
    private var mSimpleRate: Int = 1
    private val mAmplitudeSimples = ArrayList<Int>(50)
    private var loopDisposable: Disposable? = null

    /**
     *
     * @param path 录音的路径
     * @param simpleInterval 录音时振幅的取样频率，如果小于0表示不取样
     * @param simpleRate 录音时振幅的取样频率的取样频率，默认为1。为什么会有这个参数？因为MediaRecorder只支持getMaxAmplitude，
     * 要想取到精确的amplitude，只能对MaxAmplitude再取样。
     */
    // 开始录音
    @JvmOverloads
    fun startRecording(path: String, simpleInterval: Long = -1, simpleRate: Int = 1) {
        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4) // 录音文件保存的格式，这里保存为 mp4
        mRecorder?.setOutputFile(path) // 设置录音文件的保存路径
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        mRecorder?.setAudioChannels(1)
        // 设置录音文件的清晰度
        mRecorder?.setAudioSamplingRate(44100)
        mRecorder?.setAudioEncodingBitRate(192000)
        this.path = path
        mSimpleInterval = simpleInterval
        mSimpleRate = simpleRate
        try {
            mRecorder?.prepare()
            mRecorder?.start()
            if (mSimpleInterval > 0) {
                recordLoop()
            }
        } catch (e: Exception) {
            e.printAndDie()
        }
        isRecording = true
    }

    private fun recordLoop() {
        loopDisposable = Observable.interval(mSimpleInterval / mSimpleRate, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                mAmplitudeSimples.add(amplitude)
                if (mAmplitudeSimples.size >= mSimpleRate) {
                    var total = 0
                    for (i in mAmplitudeSimples) {
                        total += i
                    }
                    mAmplitudeData.add((total * 1.0f / mSimpleRate).toInt())
                    mAmplitudeSimples.clear()
                }
            }) { obj: Throwable -> obj.printStackTrace() }
    }

    /**
     * 获取振幅
     * @return MaxAmplitude
     */
    val amplitude: Int
        get() = if (mRecorder == null) {
            0
        } else mRecorder!!.maxAmplitude

    /**
     * 获取采样的振幅数据
     * @return
     */
    val amplitudeData: List<Int>
        get() = mAmplitudeData

    // 停止录音
    fun stopRecording() {
        if (mRecorder != null) {
            try {
                mRecorder!!.stop()
                mRecorder!!.release()
                mRecorder = null
            } catch (e: Exception) {
            }
        }
        path = ""
        isRecording = false
        if (loopDisposable != null) {
            loopDisposable!!.dispose()
        }
        mAmplitudeData.clear()
    }

    companion object {
        private const val TAG = "AudioRecordManager"

        @get:Synchronized
        var instance: AudioRecordManager? = null
            get() {
                if (field == null) {
                    field = AudioRecordManager()
                }
                return field
            }
            private set
        var LISTENER_INTERVAL = 32L
    }
}