package com.wedream.demo.render

import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity

class DrawPathActivity : BaseActivity() {

    var demoView: DrawPathView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draw_path)
        demoView = findViewById(R.id.demo_view)
        findViewById<SeekBar>(R.id.seek).setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                demoView?.setVoiceWeakenLength(progress.toFloat())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}