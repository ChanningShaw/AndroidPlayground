package com.wedream.demo.jni

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity


class HelloJNIActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hello_jni)
//        text.text = "1 + 2 is ${NativeCalculator.add(1, 2)}"
    }
}