package com.wedream.demo.reflection.dynamicproxy

import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.LogUtils.log
import java.lang.reflect.Proxy

class DynamicProxyActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val button = Button(this)
        button.text = "点击我执行动态代理"
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.WRAP_CONTENT,
            FrameLayout.LayoutParams.WRAP_CONTENT
        )
        setContentView(button, params)
        button.setOnClickListener {
            val bookSeller = BookFactory()
            val price = SellProxy(bookSeller).sellBook(10)
            log { "price = $price" }
        }
    }
}