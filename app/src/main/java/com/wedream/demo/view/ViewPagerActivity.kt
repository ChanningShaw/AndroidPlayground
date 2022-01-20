package com.wedream.demo.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.app.track.logWhenShow

class ViewPagerActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager)
        findViewById<ViewPager>(R.id.view_pager)?.let {
            val list = listOf("1", "2", "3")
            val adapter = SimpleAdapter()
            it.adapter = adapter
            adapter.setData(list)
        }
    }
}

class SimpleAdapter : PagerAdapter(){
    val data = mutableListOf<String>()

    fun setData(data: List<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.item_view_pager, null)
        view.findViewById<TextView>(R.id.tv).text = data[position]
        container.addView(view)
        view.tag = data[position]
        view.logWhenShow("view_show") {
            this["page"] = data[position]
        }
        return view
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView((`object` as View))
    }
}