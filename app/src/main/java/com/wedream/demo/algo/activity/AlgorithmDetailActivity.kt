package com.wedream.demo.algo.activity

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.tencent.mmkv.MMKV
import com.wedream.demo.R
import com.wedream.demo.algo.algo.LinearAlgorithm
import com.wedream.demo.algo.algo.TreeAlgorithm
import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.Option

class AlgorithmDetailActivity : AppCompatActivity() {

    lateinit var titleView: TextView
    private lateinit var tipsView: TextView
    lateinit var executeButton: Button
    lateinit var inputView: TextView
    private lateinit var outputView: TextView
    lateinit var optionsSelector: Spinner
    private val mmkv = MMKV.mmkvWithID("algo")

    companion object {
        const val KEY_ALGO_TYPE = "key_algo_type"
        const val KEY_INDEX = "index"

        const val TYPE_LINEAR = "linear"
        const val TYPE_TREE = "tree"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var algoType = intent.extras?.getString(KEY_ALGO_TYPE, TYPE_LINEAR) ?: ""
        var index = intent.extras?.getInt(KEY_INDEX, -1) ?: -1

        if (algoType.isEmpty()) {
            algoType = mmkv.getString(KEY_ALGO_TYPE, TYPE_LINEAR) ?: TYPE_LINEAR
        } else {
            mmkv.putString(KEY_ALGO_TYPE, algoType)
        }
        if (index == -1) {
            index = mmkv.getInt(KEY_INDEX, -1)
        } else {
            mmkv.putInt(KEY_INDEX, index)
        }

        val models = when (algoType) {
            TYPE_LINEAR -> {
                LinearAlgorithm.getModels()
            }
            TYPE_TREE -> {
                TreeAlgorithm.getModels()
            }
            else -> {
                LinearAlgorithm.getModels()
            }
        }

        val model = models[index]
        initViews(model)
    }

    private fun initViews(model: AlgorithmModel) {
        setContentView(R.layout.activity_algorithm_detail)
        titleView = findViewById(R.id.title_content)
        optionsSelector = findViewById(R.id.options_selector)
        tipsView = findViewById(R.id.tips_content)
        executeButton = findViewById(R.id.execute)
        inputView = findViewById(R.id.input_content)
        outputView = findViewById(R.id.output_content)
        titleView.text = model.title
        tipsView.text = model.tips

        val options = model.getOptions()
        if (options.isNotEmpty()) {
            val items = options.map {
                it.name
            }
            val adapter =
                object : ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items) {

                }
            optionsSelector.adapter = adapter
        } else {
            optionsSelector.visibility = View.GONE
        }

        executeButton.setOnClickListener {
            val item = optionsSelector.selectedItemPosition
            var option: Option? = null
            if (item >= 0) {
                option = options[item]
            }
            val result = model.execute(option)
            inputView.text = result.input
            outputView.text = result.output
        }
    }
}