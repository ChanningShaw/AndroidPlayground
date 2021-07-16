package com.wedream.demo.render

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import kotlin.random.Random

class RecyclerViewActivity : BaseActivity() {

    var recyclerView: RecyclerView? = null
    private val adapter = SimpleAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        recyclerView = findViewById(R.id.recycler)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = GridLayoutManager(this, 4)
        adapter.setData(getData())
    }

    private fun getData(): List<SimpleAdapter.SimpleData> {
        val data = mutableListOf<SimpleAdapter.SimpleData>()
        val random = Random(System.currentTimeMillis())
        for (i in 0..200) {
            val color = when (random.nextInt(3)) {
                0 -> R.color.color_blue
                1 -> R.color.color_violet
                2 -> R.color.color_green
                else -> R.color.colorAccent
            }
            data.add(SimpleAdapter.SimpleData(i, color, 0))
        }
        return data
    }
}