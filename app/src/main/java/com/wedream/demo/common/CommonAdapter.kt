package com.wedream.demo.common

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**
 * @author xiaochunming
 *
 * 一个用于实现类似于tab页选择的adapter，只进行了简单的封装，不提供任何具体的viewholder。
 * 当一个list中选择的项是高亮的话，可以用这个adapter，比较适合用于加载本地资源。具体的效果可以参考画面调节（@see PictureAdjustAdapter）。
 *
 * 提供以下功能：
 * 1.数据的封装
 * 2.点击事件的回调，点击时，整一个viewholder都会处于selected的状态
 * 3.支持设置同一个页面显示的元素个数
 *
 */
abstract class CommonAdapter<DATA, H : RecyclerView.ViewHolder>(private var context: Context) : RecyclerView.Adapter<H>() {

    protected val dataList: MutableList<DATA> = CopyOnWriteArrayList()

    private var itemClickListener: OnItemClickListener<DATA, H>? = null

    private var selectedPosition: Int = 0
    private var itemPerPage: Float = -1f

    companion object {
        const val DEFAULT_ITEM_PER_PAGE: Float = 5.5F
    }

    fun setData(data: List<DATA>) {
        dataList.clear()
        dataList.addAll(data)
        notifyDataSetChanged()
    }

    fun getData(): List<DATA> {
        return Collections.unmodifiableList(dataList)
    }

    fun setItemClickListener(listener: OnItemClickListener<DATA, H>) {
        itemClickListener = listener
    }

    fun setSelectedIndex(index: Int) {
        selectedPosition = index
        notifyDataSetChanged()
    }

    fun setItemPerPage(num: Float) {
        itemPerPage = num
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): H {
        val view = LayoutInflater.from(context).inflate(getItemLayout(), parent, false)
        if (itemPerPage > 0) {
            val layoutParams = view.layoutParams;
            layoutParams.width = ((parent.width - parent.paddingLeft - parent.paddingRight) / itemPerPage).toInt()
        }
        val holder = getViewHolder(view)
        view.tag = holder
        return holder
    }

    @CallSuper
    override fun onBindViewHolder(holder: H, position: Int) {
        holder.itemView.setOnClickListener(viewClickListener)
        holder.itemView.isSelected = selectedPosition == position
    }

    private val viewClickListener = View.OnClickListener {
        val holder = it.tag as H
        val pos = holder.adapterPosition
        itemClickListener?.onItemClick(dataList[pos], holder, pos)
        selectedPosition = pos
        notifyDataSetChanged()
    }

    abstract fun getItemLayout(): Int
    abstract fun getViewHolder(view: View): H

    interface OnItemClickListener<DATA, H> {
        fun onItemClick(data: DATA, holder: H, pos: Int)
    }
}
