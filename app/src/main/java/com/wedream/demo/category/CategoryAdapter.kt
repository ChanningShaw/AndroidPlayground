package com.wedream.demo.category

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.common.CommonAdapter

class CategoryAdapter(context: Context) : CommonAdapter<Category, CategoryAdapter.Holder>(context) {

    class Holder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = view.findViewById(R.id.tv)
        val childCount: TextView = view.findViewById(R.id.child_count)
    }

    override fun getItemLayout(): Int {
        return R.layout.item_category_list
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        super.onBindViewHolder(holder, position)
        val data = getData()[position]
        holder.textView.text = data.name
        if (data is ComponentCategory) {
            holder.childCount.visibility = View.GONE
        } else if (data.getChildCount() > 0) {
            holder.childCount.text = "共有${data.getChildCount()}个demo"
            holder.childCount.visibility = View.VISIBLE
        } else {
            holder.childCount.visibility = View.GONE
        }
    }

    override fun getViewHolder(view: View): Holder {
        return Holder(view)
    }
}