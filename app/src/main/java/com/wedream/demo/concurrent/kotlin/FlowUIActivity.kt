package com.wedream.demo.concurrent.kotlin

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class FlowUIActivity : BaseActivity() {

    private val _uiState = MutableStateFlow("")
    val uiState: StateFlow<String> = _uiState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_ui)
        var count = 0
        findViewById<Button>(R.id.bt1).setOnClickListener {
            _uiState.value = (++count).toString()
        }
        val textView1 = findViewById<TextView>(R.id.text1)
        lifecycleScope.launch {
            uiState.collect {
                textView1.text = it
            }
        }
        val textView2 = findViewById<TextView>(R.id.text2)
        lifecycleScope.launch {
            uiState.collect {
                textView2.text = it
                if (it == "5") {
                    cancel()
                }
            }
        }
    }
}