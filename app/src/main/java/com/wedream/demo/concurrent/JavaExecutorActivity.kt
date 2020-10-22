package com.wedream.demo.concurrent

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import java.util.concurrent.Executors

class JavaExecutorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_java_executor)
        findViewById<Button>(R.id.start_executor).setOnClickListener {
            val executorService = Executors.newFixedThreadPool(5)
            repeat(10) {
                executorService.submit {
                    log {
                        "task run"
                    }
                }
            }
        }
    }
}