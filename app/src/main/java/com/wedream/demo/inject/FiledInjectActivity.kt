package com.wedream.demo.inject

import android.os.Bundle
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.LogUtils.log
import java.lang.reflect.Modifier

class FiledInjectActivity : BaseActivity() {

    private val delegate = Delegate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}