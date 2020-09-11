package com.wedream.demo.render

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.ArrayUtils

class WaveViewActivity : AppCompatActivity() {

    private var waveView: WaveView? = null
    private var seekBar: SeekBar? = null

    private val benchMark1 = arrayOf(
        0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 20, 0, 0, 0, 0, 100, 0, 0, 0, 10,
        0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 10, 0, 0, 0, 0, 100, 0, 0, 0, 0)

    private val benchMark2 = arrayOf(
        0, 0, 80, 50, 90, 100, 80, 50, 90, 100, 88, 95, 100, 50, 60, 70, 80, 100, 88, 60, 100,
        70, 60, 80, 50, 90, 100, 80, 50, 90, 100, 88, 95, 100, 50, 60, 70, 80, 100, 88, 60, 0, 0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wave_view)
        waveView = findViewById(R.id.wave_view)
        findViewById<Button>(R.id.bt_random).setOnClickListener {
            val data = ArrayUtils.randomArray(100, seekBar?.progress ?: 50, 101)
            waveView?.setData(data)
        }
        findViewById<Button>(R.id.bt_linear).setOnClickListener {
            waveView?.setType(WaveView.Type.Linear)
        }
        findViewById<Button>(R.id.bt_square).setOnClickListener {
            waveView?.setType(WaveView.Type.Square)
        }
        findViewById<Button>(R.id.bt_rank_square).setOnClickListener {
            waveView?.setType(WaveView.Type.RankSquare)
        }
        findViewById<Button>(R.id.bt_rank_linear).setOnClickListener {
            waveView?.setType(WaveView.Type.RankLinear)
        }
        findViewById<Button>(R.id.bt_up_cubic).setOnClickListener {
            waveView?.setType(WaveView.Type.UpCubic)
        }
        findViewById<Button>(R.id.bt_down_cubic).setOnClickListener {
            waveView?.setType(WaveView.Type.DownCubic)
        }
        findViewById<Button>(R.id.bt_benchmark1).setOnClickListener {
            waveView?.setData(benchMark1)
        }
        findViewById<Button>(R.id.bt_benchmark2).setOnClickListener {
            waveView?.setData(benchMark2)
        }
        seekBar = findViewById(R.id.seek)
        seekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
    }
}