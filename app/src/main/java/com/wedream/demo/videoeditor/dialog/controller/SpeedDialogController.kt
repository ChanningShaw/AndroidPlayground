package com.wedream.demo.videoeditor.dialog.controller

import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.inject.Inject
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.editor.EditorGovernor
import com.wedream.demo.videoeditor.editor.VideoEditor
import com.wedream.demo.videoeditor.editor.action.Action
import com.wedream.demo.videoeditor.message.MessageChannel
import com.wedream.demo.videoeditor.message.TimeLineMessageHelper
import com.wedream.demo.videoeditor.project.asset.operation.ISpeed
import com.wedream.demo.videoeditor.timeline.data.Segment
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel

class SpeedDialogController : DialogController() {

    @Inject
    lateinit var editorGovernor: EditorGovernor

    @Inject
    lateinit var timelineViewModel: TimelineViewModel

    var currentSegmentId = 0L

    lateinit var confirmBtn: View
    lateinit var seekBar: SeekBar
    lateinit var speedTextView: TextView

    override fun onBind() {
        initViews()
        currentSegmentId = getInjectedObject(Segment::class.java)?.id ?: 0L
        initSeekBar(currentSegmentId)
        initListeners()
    }
    private fun initViews() {
        confirmBtn = findViewById(R.id.confirm_bt)
        seekBar = findViewById(R.id.speed_seek_bar)
        speedTextView = findViewById(R.id.speed_text)
        confirmBtn.setOnClickListener {
            dialog.dismiss()
        }
        seekBar.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                val speed = progressToSpeed(seekBar.progress)
                speedTextView.text = speed.toString()
                editorGovernor.handleAction(Action.SpeedAction(currentSegmentId, speed))
            }
        })
    }

    private fun initSeekBar(id: Long){
        val currentSpeed = (editorGovernor.getAsset(id) as? ISpeed)?.getSpeed() ?: 1.0
        speedTextView.text = currentSpeed.toString()
        val value = speedToProgress(currentSpeed)
        seekBar.progress = value
    }

    private fun initListeners() {
        MessageChannel.subscribe(TimeLineMessageHelper.MSG_TIMELINE_CHANGED) {
            currentSegmentId = timelineViewModel.getCurrentSegment()?.id ?: 0L
            initSeekBar(currentSegmentId)
        }
    }

    private fun speedToProgress(speed: Double): Int {
        return when {
            speed < 1.0 -> {
                ((speed - 0.1) / 0.9 * 25).toInt()
            }
            speed >= 1.0 && speed < 2.0 -> {
                25 + ((speed - 1.0) / 1.0 * 25).toInt()
            }
            speed >= 2.0 && speed < 5.0 -> {
                50 + ((speed - 2.0) / 3.0 * 25).toInt()
            }
            speed >= 5.0 -> {
                75 + ((speed - 5.0) / 5 * 25).toInt()
            }
            else -> {
                0
            }
        }
    }

    private fun progressToSpeed(progress: Int): Double {
        val speed =  when {
            progress < 25 -> {
                0.1 + (progress - 0.0) / 25 * 0.9
            }
            progress >= 25 && progress < 50 -> {
                1.0 + (progress - 25.0) / 25 * 1.0
            }
            progress >= 50 && progress < 75 -> {
                2.0 + (progress - 50.0) / 25 * 3.0
            }
            progress >= 75 -> {
                5.0 + (progress - 75.0) / 25 * 5.0
            }
            else -> {
                1.0
            }
        }
        return (speed * 100).toInt() / 100.0
    }
}