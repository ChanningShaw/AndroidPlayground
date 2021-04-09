package com.wedream.demo.algo.activity

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.algo.algo.ArrayAlgorithm

class AlgorithmDetailActivity : AppCompatActivity() {

    lateinit var titleView: TextView
    private lateinit var tipsView: TextView
    lateinit var executeButton: Button
    lateinit var inputView: TextView
    lateinit var outputView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_algorithm_detail)
        titleView = findViewById(R.id.title_content)
        tipsView = findViewById(R.id.tips_content)
        executeButton = findViewById(R.id.execute)
        inputView = findViewById(R.id.input_content)
        outputView = findViewById(R.id.output_content)

        val index = intent.extras?.getInt("index", 0) ?: 0
        val model = ArrayAlgorithm.getModels()[index]

        titleView.text = model.title
        tipsView.text = model.tips

        executeButton.setOnClickListener {
            val result = model.execute()
            inputView.text = result.first
            outputView.text = result.second
        }
    }
}