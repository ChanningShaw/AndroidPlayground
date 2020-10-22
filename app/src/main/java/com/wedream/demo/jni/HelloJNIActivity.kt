package com.wedream.demo.jni

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import kotlinx.android.synthetic.main.activity_hello_jni.*


class HelloJNIActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_jni)
        text.text = "1 + 2 is ${NativeCalculator.add(1, 2)}"
    }
}