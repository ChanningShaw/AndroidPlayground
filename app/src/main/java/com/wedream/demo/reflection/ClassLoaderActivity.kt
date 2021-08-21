package com.wedream.demo.reflection

import android.app.Application
import android.os.Bundle
import android.view.View
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.LogUtils.log

class ClassLoaderActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loader1 = this.classLoader
        val loader2 = Application::class.java.classLoader
        val loader3 = View::class.java.classLoader
        log { "loader1 = $loader1" }
        log { "loader2 = $loader2" }
        log { "loader3 = $loader3" }
    }
}