package com.wedream.demo.view.newtips

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import com.wedream.demo.R
import com.wedream.demo.app.ApplicationHolder
import com.wedream.demo.app.BaseActivity

class NewTipsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_tips)
        val tv1 = findViewById<TextView>(R.id.tv1)
        val tipsView1 = NewTipsView(this, "bt1", NewTipsView.TipType.TYPE_DOT)
        tipsView1.bind(tv1, 5, 5)
        tv1.setOnClickListener {
            tipsView1.update()
        }

        val tv2 = findViewById<TextView>(R.id.tv2)
        val tipsView2 = NewTipsView(this, "bt2", NewTipsView.TipType.TYPE_DOT)
        tipsView2.bind(tv2, 5, 5, tipsView1.spKey)
        tv2.setOnClickListener {
            tipsView2.update()
        }

        val tv3 = findViewById<TextView>(R.id.tv3)
        val tipsView3 = NewTipsView(this, "bt3", NewTipsView.TipType.TYPE_DOT)
        tipsView3.bind(tv3, 5, 5, tipsView2.spKey)
        tv3.setOnClickListener {
            tipsView3.update()
        }

        val tv4 = findViewById<TextView>(R.id.tv4)
        val tipsView4 = NewTipsView(this, "bt4", NewTipsView.TipType.TYPE_DOT)
        tipsView4.bind(tv4, 5, 5, tipsView3.spKey)
        tv4.setOnClickListener {
            tipsView4.update()
        }

        val tv5 = findViewById<TextView>(R.id.tv5)
        val tipsView5 = NewTipsView(this, "bt5", NewTipsView.TipType.TYPE_DOT)
        tipsView5.bind(tv5, 5, 5, tipsView1.spKey)
        tv5.setOnClickListener {
            tipsView5.update()
        }

        val tv6 = findViewById<TextView>(R.id.tv6)
        val tipsView6 = NewTipsView(this, "bt6", NewTipsView.TipType.TYPE_DOT)
        tipsView6.bind(tv6, 5, 5, tipsView2.spKey)
        tv6.setOnClickListener {
            tipsView6.update()
        }

        val tv7 = findViewById<TextView>(R.id.tv7)
        val tipsView7 = NewTipsView(this, "bt7", NewTipsView.TipType.TYPE_DOT)
        tipsView7.bind(tv7, 5, 5, tipsView3.spKey)
        tv7.setOnClickListener {
            tipsView7.update()
        }

        val tv8 = findViewById<TextView>(R.id.tv8)
        val tipsView8 = NewTipsView(this, "bt8", NewTipsView.TipType.TYPE_DOT)
        tipsView8.bind(tv8, 5, 5, tipsView4.spKey)
        tv8.setOnClickListener {
            tipsView8.update()
        }

        val inputText = findViewById<EditText>(R.id.tv_add_node)
        val linearLayout = findViewById<LinearLayout>(R.id.ll)

        val button = findViewById<Button>(R.id.bt_add).setOnClickListener {
            if (!TextUtils.isEmpty(inputText.text)) {
                val view = LayoutInflater.from(ApplicationHolder.instance).inflate(R.layout.item_newtips_list, null)
                linearLayout.addView(view)
                val textView = view.findViewById<TextView>(R.id.tv)
                val key = System.currentTimeMillis().toString()
                val tipsView = NewTipsView(it.context, key, NewTipsView.TipType.TYPE_DOT)
                when (inputText.text.trim().toString().toInt()) {
                    0 -> {
                        tipsView.bind(textView, 5, 5)
                        textView.text = "父节点：root"
                    }
                    1 -> {
                        tipsView.bind(textView, 5, 5, "bt1")
                        textView.text = "父节点：A"
                    }
                    2 -> {
                        tipsView.bind(textView, 5, 5, "bt2")
                        textView.text = "父节点：B"
                    }
                    3 -> {
                        tipsView.bind(textView, 5, 5, "bt3")
                        textView.text = "父节点：C"
                    }
                    4 -> {
                        tipsView.bind(textView, 5, 5, "bt4")
                        textView.text = "父节点：D"
                    }
                    5 -> {
                        tipsView.bind(textView, 5, 5, "bt5")
                        textView.text = "父节点：E"
                    }
                    6 -> {
                        tipsView.bind(textView, 5, 5, "bt6")
                        textView.text = "父节点：F"
                    }
                    7 -> {
                        tipsView.bind(textView, 5, 5, "bt7")
                        textView.text = "父节点：G"
                    }
                    8 -> {
                        tipsView.bind(textView, 5, 5, "bt8")
                        textView.text = "父节点：H"
                    }
                    else -> {
                        tipsView.bind(textView, 5, 5)
                        textView.text = "父节点：root"
                    }
                }
                textView.setOnClickListener {
                    tipsView.update()
                }
            }
        }
    }
}