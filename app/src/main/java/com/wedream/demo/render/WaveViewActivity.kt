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

    companion object {
        val benchMark1 = arrayOf(
            0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 20, 0, 0, 0, 0, 100, 0, 0, 0, 10,
            0, 0, 0, 0, 0, 0, 100, 0, 0, 0, 10, 0, 0, 0, 0, 100, 0, 0, 0, 0
        )

        val benchMark2 = arrayOf(
            0, 0, 80, 50, 90, 100, 80, 50, 90, 100, 88, 95, 100, 50, 60, 70, 80, 100, 88, 60, 100,
            70, 60, 80, 50, 90, 100, 80, 50, 90, 100, 88, 95, 100, 50, 60, 70, 80, 100, 88, 60, 0, 0
        )

        val benchMark3 = arrayOf(
            0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27,
            28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43 ,44, 45, 46, 47, 48, 49, 50
        )
    }

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