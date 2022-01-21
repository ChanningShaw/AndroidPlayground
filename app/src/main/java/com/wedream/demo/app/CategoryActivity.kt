package com.wedream.demo.app

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.ss.android.ugc.aweme.ecommerce.trackimpl.setPreviousTrackNode
import com.wedream.demo.R
import com.wedream.demo.app.track.TrackParams
import com.wedream.demo.app.track.logEvent
import com.wedream.demo.category.Category
import com.wedream.demo.category.CategoryAdapter
import com.wedream.demo.category.ComponentCategory
import com.wedream.demo.common.CommonAdapter

open class CategoryActivity : BaseActivity() {

    private var data: List<Category>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        data = intent.extras?.getParcelableArrayList("categories")
        data?.let {
            setCategoryList(it)
        }
    }

    fun setCategoryList(categories: List<Category>) {
        val rv = findViewById<RecyclerView>(R.id.main_rv)
        rv.setTag(R.id.lib_track_tag_id_params_source, this)
        val adapter = CategoryAdapter(this, this)
        rv.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter.setData(categories)
        rv.adapter = adapter

        adapter.setItemClickListener(object :
            CommonAdapter.OnItemClickListener<Category, CategoryAdapter.Holder> {
            override fun onItemClick(data: Category, holder: CategoryAdapter.Holder, pos: Int) {
                onCategoryClick(data, pos)
                logEvent("click") {
                    this["pos"] = pos.toString()
                }
            }
        })
        rv.logEvent("rv_show") {
            this["data_size"] = categories.size
        }
    }

    override fun fillTrackParams(params: TrackParams) {
        params["CategoryActivity"] = "this is ${javaClass.simpleName}"
    }

    open fun onCategoryClick(data: Category, pos: Int) {
        val intent = Intent()
        if (data is ComponentCategory) {
            intent.component = data.componentName
        } else {
            intent.setClass(this@CategoryActivity, CategoryActivity::class.java)
            intent.putParcelableArrayListExtra("categories", data.getChildren())
        }
        intent.setPreviousTrackNode(this) {
            this["category"] = data.name
        }
        startActivity(intent)
    }
}
