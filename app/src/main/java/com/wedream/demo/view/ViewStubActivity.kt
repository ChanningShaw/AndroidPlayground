package com.wedream.demo.view

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.common.CommonAdapter

class ViewStubActivity : BaseActivity() {

    private val adapter = ViewStubAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler)
        initView()
    }

    private fun initView() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler)
        recyclerView?.adapter = adapter
        recyclerView?.layoutManager = GridLayoutManager(this, 1)
        adapter.setData(getData())
    }

    private fun getData(): List<Data> {
        val data = mutableListOf<Data>()
        for (i in 0..200) {
//            Random(System.currentTimeMillis()).nextInt(2))
            data.add(Data(i, i))
        }
        return data
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val v1 = itemView.findViewById<View>(R.id.v1)
        val v2 = itemView.findViewById<View>(R.id.v2)
    }
}

class ViewStubAdapter(context: Context) : CommonAdapter<Data, ViewStubActivity.Holder>(context) {
    override fun getItemLayout(): Int {
        return R.layout.item_view_stub
    }

    override fun getViewHolder(view: View): ViewStubActivity.Holder {
        return ViewStubActivity.Holder(view)
    }

    override fun onBindViewHolder(holder: ViewStubActivity.Holder, position: Int) {
        super.onBindViewHolder(holder, position)
        val data = getData()[position]
        val color = when (data.index % 3) {
            0 -> R.color.color_blue
            1 -> R.color.color_violet
            2 -> R.color.color_green
            else -> R.color.color_green
        }
        holder.itemView.setBackgroundResource(color)
        when (data.showIndex % 4) {
            0 -> {
                holder.v1.isVisible = true
                holder.v2.isVisible = true
            }
            1 -> {
                holder.v1.isVisible = true
                holder.v2.isVisible = false
            }
            2 -> {
                holder.v1.isVisible = false
                holder.v2.isVisible = true
            }
            3 -> {
                holder.v1.isVisible = false
                holder.v2.isVisible = false
            }
        }
    }
}

data class Data(val index: Int, val showIndex: Int)