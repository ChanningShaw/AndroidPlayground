package com.wedream.demo.reflection

import android.os.Bundle
import com.wedream.demo.app.BaseActivity

class FiledInjectActivity : BaseActivity() {

    private val delegate = Delegate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}