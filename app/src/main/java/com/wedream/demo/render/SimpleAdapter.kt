package com.wedream.demo.render

import android.content.Context
import android.os.Handler
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.app.track.logWhenShow
import com.wedream.demo.common.CommonAdapter
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.view.TasksCompletedView

class SimpleAdapter(context: Context) : CommonAdapter<SimpleAdapter.SimpleData, SimpleAdapter.Holder>(context) {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
        val taskView: TasksCompletedView = itemView.findViewById(R.id.task_view)
        var downloadPos = -1
        var newPos = -1
    }

    private val handler = Handler()

    override fun onViewRecycled(holder: Holder) {
        super.onViewRecycled(holder)
        holder.taskView.visibility = View.GONE
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        super.onBindViewHolder(holder, position)
        val data = getData()[position]
        holder.text.text = data.n.toString()
        holder.itemView.setBackgroundResource(data.colorRes)
        holder.newPos = position
        holder.itemView.tag = data.n
        holder.itemView.setOnClickListener {
//            holder.downloadPos = position
//            downloadDemo(0, data, holder)
        }
        holder.itemView.logWhenShow("itemView_show")
    }

    override fun getItemLayout(): Int {
        return R.layout.item_simple
    }

    override fun getViewHolder(view: View): Holder {
        return Holder(view)
    }

    class SimpleData(val n: Int, val colorRes: Int, var downloadProgress: Int)

    private fun downloadDemo(i: Int, data: SimpleData, holder: Holder) {
        log { "i = $i, newPos = ${holder.newPos}, downloadPos = ${holder.downloadPos}" }
        if (i <= 100 && holder.downloadPos == holder.newPos) {
            holder.taskView.setProgress(i.toFloat())
            holder.taskView.visibility = View.VISIBLE
        } else {
            holder.taskView.visibility = View.GONE
            return
        }
        handler.postDelayed({
            downloadDemo(i + 1, data, holder)
        }, 100)
    }
}