package com.wedream.demo.render

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.app.track.TrackParams
import com.wedream.demo.app.track.logEvent

class MatrixDemoActivity : BaseActivity() {

    var demoView: MatrixDemoView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_matrix_demo)
        demoView = findViewById(R.id.demo_view)
        findViewById<SeekBar>(R.id.seek).setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val value = progress - 90
                demoView?.setRotate(value.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }

    override fun fillTrackParams(params: TrackParams) {
        params["MatrixDemoActivity"] = "this is ${javaClass.simpleName}"
    }

    override fun onResume() {
        super.onResume()
        logEvent("page_show")
    }
}