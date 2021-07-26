package com.wedream.demo.inject

import android.os.Bundle
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.LogUtils.log
import java.lang.reflect.Modifier

class FiledInjectActivity : BaseActivity() {

    private val delegate = Delegate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject()
    }

    private fun inject() {
        val clazz = this.javaClass
        val fields = clazz.declaredFields
        for (field in fields) {
            if (Delegate::class.java.isAssignableFrom(field.type)) {
                field.isAccessible = true
                log { "get = ${field.get(this)}" }
            }
        }
    }
}