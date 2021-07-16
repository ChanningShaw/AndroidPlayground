package com.wedream.demo.concurrent.kotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.LogUtils.log

class FunctionProgrammingActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function_programming)
        findViewById<Button>(R.id.bt_function).setOnClickListener {
            onClick { }
            log { "lick" }
        }

        let {

        }

        let2 {

        }

        var a = let1 {
            1
            "a"
        }
        log { a.javaClass.canonicalName }
    }


    private var add: (Int, Int) -> Int = { a, b ->
        a + b
    }

    private var print: ((Any) -> Unit)? = null

    private var listener: (View) -> Unit = {
        print?.invoke("dddd")
        add(1, 2)
    }

    private var block: () -> Unit = {

    }

    fun onClick(block: View.() -> Unit) {

    }

    fun <T, R> T.let1(m: (T) -> R): R {
        return m(this)
    }

    fun <T, R> T.let2(m: T.() -> R): R {
        return m()
    }
}
