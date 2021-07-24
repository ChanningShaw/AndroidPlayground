package com.wedream.demo.algo.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import com.wedream.demo.R
import com.wedream.demo.algo.playground.AlgorithmRunner
import com.wedream.demo.algo.playground.LinkedListAlgorithm.deleteK
import com.wedream.demo.algo.playground.LinkedListAlgorithm.deleteLastK
import com.wedream.demo.algo.playground.LinkedListAlgorithm.randomLinkedList
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.algo.view.LinkedListVisualizationView
import com.wedream.demo.app.BaseActivity
import kotlin.random.Random

class LinkedListActivity : BaseActivity() {

    private var demoView: LinkedListVisualizationView? = null
    private var runner = AlgorithmRunner()
    private var data = LinkedList<Int>()

    companion object {
        const val EL_SIZE = 8
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linked_list)
        findViewById<Button>(R.id.reset).setOnClickListener {
            data = randomLinkedList(EL_SIZE)
            demoView?.setData(data)
        }
        findViewById<Button>(R.id.start).setOnClickListener {
            demoView?.let {
                runner.start(it, algorithmCall)
            }
        }
        demoView = findViewById(R.id.ll_demo_view)
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

                when (position) {
                    0 -> {
                        algorithmCall = { channel ->
                            val random = Random(System.currentTimeMillis())
                            deleteK(
                                data,
                                random.nextInt(1, EL_SIZE + 1),
                                channel
                            )
                        }
                    }
                    1 -> {
                        algorithmCall = { channel ->
                            val random = Random(System.currentTimeMillis())
                            deleteLastK(
                                data,
                                random.nextInt(1, EL_SIZE + 1),
                                channel
                            )
                        }
                    }
                    else -> {

                    }
                }
            }
        }
    }

    private lateinit var algorithmCall: suspend (AlgorithmRunner.ChannelWrap) -> Unit
}