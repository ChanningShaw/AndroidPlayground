package com.wedream.demo.app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.category.Category
import com.wedream.demo.category.CategoryAdapter
import com.wedream.demo.category.ComponentCategory
import com.wedream.demo.common.CommonAdapter

open class CategoryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)
        val data = intent.extras?.getParcelableArrayList<Category>("categories") as List<Category>?
        data?.let {
            setCategoryList(data)
        }
    }

    fun setCategoryList(categories: List<Category>) {
        val rv = findViewById<RecyclerView>(R.id.main_rv)
        val adapter = CategoryAdapter(this)
        rv.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        adapter.setData(categories)
        rv.adapter = adapter

        adapter.setItemClickListener(object :
            CommonAdapter.OnItemClickListener<Category, CategoryAdapter.Holder> {
            override fun onItemClick(data: Category, holder: CategoryAdapter.Holder, pos: Int) {
                onCategoryClick(data, pos)
            }
        })
    }

    open fun onCategoryClick(data: Category, pos: Int) {
        val intent = Intent()
        if (data is ComponentCategory) {
            intent.component = data.componentName
        } else {
            intent.setClass(this@CategoryActivity, CategoryActivity::class.java)
            intent.putParcelableArrayListExtra("categories", data.getChildren())
        }
        startActivity(intent)
    }
}
