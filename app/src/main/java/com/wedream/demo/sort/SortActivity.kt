package com.wedream.demo.sort

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R

class SortActivity : AppCompatActivity() {

    var sortView: SortVisualizationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sort)
        sortView = findViewById(R.id.sort_view)
        findViewById<Button>(R.id.reset).setOnClickListener {
            sortView?.reset()
        }
        findViewById<Button>(R.id.start).setOnClickListener {
            sortView?.startSort()
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
                    0 -> SortAlgorithm.Type.Bubble
                    1 -> SortAlgorithm.Type.Select
                    2 -> SortAlgorithm.Type.Insert
                    3 -> SortAlgorithm.Type.Shell
                    4 -> SortAlgorithm.Type.Merge
                    5 -> SortAlgorithm.Type.Quick
                    else -> SortAlgorithm.Type.Bubble
                }
                sortView?.setAlgorithm(algo)
            }
        }
    }

    override fun onStop() {
        super.onStop()
    }
}