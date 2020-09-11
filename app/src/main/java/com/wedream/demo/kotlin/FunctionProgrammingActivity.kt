package com.wedream.demo.kotlin

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import kotlinx.coroutines.CoroutineScope

class FunctionProgrammingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_function_programming)
        findViewById<Button>(R.id.bt_function).setOnClickListener {
            onClick {  }
        }
    }


    private var add: (Int, Int) -> Int = { a, b ->
        a + b
    }

    private var print: ((Any) -> Unit)? = null

    private var listener:(View) -> Unit = {
        print?.invoke("dddd")
        add(1, 2)
    }

    private var block:() -> Unit = {

    }

    fun onClick(block: View.() -> Unit){

    }
}
