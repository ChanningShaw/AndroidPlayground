package com.wedream.demo.algo.activity

import android.content.Intent
import android.os.Bundle
import com.wedream.demo.algo.algo.ArrayAlgorithm
import com.wedream.demo.app.CategoryActivity
import com.wedream.demo.category.Category

class ArrayAlgorithmActivity() : CategoryActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCategoryList(buildData())
    }

    private fun buildData(): List<Category> {
        return ArrayAlgorithm.getModels().map {
            Category(it.name)
        }
    }

    override fun onCategoryClick(data: Category, pos: Int) {
        val intent = Intent(this, AlgorithmDetailActivity::class.java)
        intent.putExtra("index", pos)
        startActivity(intent)
    }
}