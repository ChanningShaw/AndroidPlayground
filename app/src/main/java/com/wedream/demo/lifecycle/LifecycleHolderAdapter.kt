package com.wedream.demo.lifecycle

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.LiveData
import com.wedream.demo.R
import com.wedream.demo.common.CommonAdapter
import com.wedream.demo.util.LogUtils.log

class LifecycleHolderAdapter(context: Context,
                             private val liveData: LiveData<Pair<Int, Int>>) : CommonAdapter<LifecycleHolderAdapter.SimpleData, LifecycleHolderAdapter.Holder>(context) {

    class Holder(itemView: View) : LifecycleHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.text)
        var newPos = -1
    }

    override fun onViewRecycled(holder: Holder) {
        super.onViewRecycled(holder)
        holder.onDestroy()
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.onResume()
        super.onBindViewHolder(holder, position)
        val data = getData()[position]
        holder.text.text = data.n.toString()
        holder.itemView.setBackgroundResource(data.colorRes)
        holder.newPos = position
        liveData.observe(holder, {
            val filed = liveData.javaClass.superclass.getDeclaredField("mActiveCount")
            filed.isAccessible  = true
            log { "liveData observer count = ${filed.get(liveData)}" }
            if (it.first == holder.newPos) {
                log { "update ${it.first} to ${it.second}" }
                holder.text.text = it.second.toString()
            }
        })
    }

    override fun getItemLayout(): Int {
        return R.layout.item_simple
    }

    override fun getViewHolder(view: View): Holder {
        return Holder(view)
    }

    class SimpleData(val n: Int, val colorRes: Int)
}