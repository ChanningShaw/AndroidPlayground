package com.wedream.demo.lifecycle

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlin.random.Random

class LifecycleHolderTestActivity : BaseActivity() {
    var recyclerView: RecyclerView? = null
    private val liveData = MutableLiveData<Pair<Int, Int>>()
    private val adapter = LifecycleHolderAdapter(this, liveData)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        recyclerView = findViewById(R.id.recycler)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = GridLayoutManager(this, 1)
        adapter.setData(getData())
        lifecycleScope.launchWhenStarted {
            while (isActive) {
                liveData.value = Pair(0, Random.nextInt(10))
                delay(1000)
            }
        }
    }

    private fun getData(): List<LifecycleHolderAdapter.SimpleData> {
        val data = mutableListOf<LifecycleHolderAdapter.SimpleData>()
        val random = Random(System.currentTimeMillis())
        for (i in 0..200) {
            val color = when (i % 3) {
                0 -> R.color.color_blue
                1 -> R.color.color_violet
                2 -> R.color.color_green
                else -> R.color.colorAccent
            }
            data.add(LifecycleHolderAdapter.SimpleData(i, color))
        }
        return data
    }
}