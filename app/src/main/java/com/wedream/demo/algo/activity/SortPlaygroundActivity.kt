package com.wedream.demo.algo.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import com.wedream.demo.R
import com.wedream.demo.algo.playground.AlgorithmRunner
import com.wedream.demo.algo.playground.SortPG
import com.wedream.demo.algo.view.SortVisualizationView
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.ArrayUtils

class SortPlaygroundActivity : BaseActivity() {

    var sortView: SortVisualizationView? = null
    var data = emptyArray<Int>()

    private var runner = AlgorithmRunner()
    var algo = SortPG.Type.Bubble

    companion object {
        const val EL_SIZE = 10
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sort)
        sortView = findViewById(R.id.sort_view)
        findViewById<Button>(R.id.reset).setOnClickListener {
            data = ArrayUtils.randomArray(EL_SIZE)
            runner.cancel()
            sortView?.reset(data)
        }
        findViewById<Button>(R.id.start).setOnClickListener {
            sortView?.let {
                runner.start(it) { channel ->
                    SortPG.sort(data, channel, algo)
                }
            }
        }
        findViewById<Button>(R.id.pause).setOnClickListener {
            it as Button
            runner.togglePause()
            if (runner.isRunning()) {
                it.text = "继续"
            } else {
                it.text = "暂停"
            }
        }
        findViewById<Spinner>(R.id.algo_selector).onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val algo = when (position) {
                    0 -> SortPG.Type.Bubble
                    1 -> SortPG.Type.Select
                    2 -> SortPG.Type.Insert
                    3 -> SortPG.Type.Shell
                    4 -> SortPG.Type.Merge
                    5 -> SortPG.Type.Quick
                    else -> SortPG.Type.Bubble
                }
                this@SortPlaygroundActivity.algo = algo
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }
}