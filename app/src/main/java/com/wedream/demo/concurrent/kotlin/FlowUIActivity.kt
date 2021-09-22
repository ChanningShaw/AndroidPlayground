package com.wedream.demo.concurrent.kotlin

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.string
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class FlowUIActivity : BaseActivity() {

    private val _uiState = MutableStateFlow<String?>("")
    private val uiState: StateFlow<String?> = _uiState
    private var eventCallbacks: ArrayList<(String) -> Boolean> = arrayListOf()
    private var liveData = getRtcEventFlow().asLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_flow_ui)
        findViewById<Button>(R.id.bt1).setOnClickListener {
            _uiState.value = null
        }
        val textView1 = findViewById<TextView>(R.id.text1)
        lifecycleScope.launch {
            uiState.collect {
                log { "collect $it" }
                textView1.text = it.toString()
            }
        }
        val textView2 = findViewById<TextView>(R.id.text2)
        lifecycleScope.launch {
            uiState.collect {
                textView2.text = it.toString()
                if (it == "5") {
                    cancel()
                }
            }
        }

        GlobalScope.launch {
            repeat(20) { n ->
                delay(500L)
                _uiState.emit("0")
//                if (n and 1 == 0) {
//                    _uiState.emit(null)
//                } else {
//                    _uiState.emit(n.toString())
//                }
            }
        }

        liveData.observe(this, {
            log { "observe : $it" }
        })
    }

    private fun getRtcEventFlow(): Flow<String> {
        return callbackFlow {
            val callback: (String) -> Boolean = {
                offer(it)
            }
            synchronized(eventCallbacks) {
                eventCallbacks.add(callback)
            }
            awaitClose {
            }
        }
    }
}