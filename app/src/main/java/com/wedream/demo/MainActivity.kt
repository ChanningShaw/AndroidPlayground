package com.wedream.demo

import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.algo.activity.LinkedListActivity
import com.wedream.demo.app.MyApplication
import com.wedream.demo.category.CategoryAdapter
import com.wedream.demo.common.CommonAdapter
import com.wedream.demo.kotlin.CoroutineActivity
import com.wedream.demo.kotlin.FlowActivity
import com.wedream.demo.kotlin.FunctionProgrammingActivity
import com.wedream.demo.planegeometry.PlaneGeometryActivity
import com.wedream.demo.render.DrawPathActivity
import com.wedream.demo.render.DrawTextDemoActivity
import com.wedream.demo.render.MatrixDemoActivity
import com.wedream.demo.render.WaveViewActivity
import com.wedream.demo.algo.activity.SortActivity
import com.wedream.demo.app.ApplicationHolder

class MainActivity : AppCompatActivity() {

    companion object {
        val data = listOf(
            MatrixDemoActivity::class.java,
            DrawTextDemoActivity::class.java,
            PlaneGeometryActivity::class.java,
            CoroutineActivity::class.java,
            FlowActivity::class.java,
            SortActivity::class.java,
            LinkedListActivity::class.java,
            DrawPathActivity::class.java,
            WaveViewActivity::class.java,
            FunctionProgrammingActivity::class.java
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lastCom = ApplicationHolder.instance.getLastResumeActivity()
        if (lastCom != componentName) {
            val intent = Intent()
            intent.component = lastCom
            startActivity(intent)
        }
        setContentView(R.layout.activity_main)
        val rv = findViewById<RecyclerView>(R.id.main_rv)
        val adapter = CategoryAdapter(this)
        rv.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter.setData(data)
        rv.adapter = adapter

        adapter.setItemClickListener(object :
            CommonAdapter.OnItemClickListener<Class<*>, CategoryAdapter.Holder> {
            override fun onItemClick(data: Class<*>, holder: CategoryAdapter.Holder, pos: Int) {
                val intent = Intent(this@MainActivity, data)
                startActivity(intent)
            }
        })
    }
}
