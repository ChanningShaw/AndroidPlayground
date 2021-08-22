package com.wedream.demo.algo.activity

import android.content.Intent
import android.os.Bundle
import com.wedream.demo.algo.algo.LinearAlgorithm
import com.wedream.demo.algo.algo.SortAlgorithm
import com.wedream.demo.algo.algo.StringAlgorithm
import com.wedream.demo.algo.algo.TreeAlgorithm
import com.wedream.demo.app.CategoryActivity
import com.wedream.demo.category.Category

class SortAlgorithmActivity : CategoryActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCategoryList(buildData())
    }

    private fun buildData(): List<Category> {
        return SortAlgorithm.getModels().map {
            Category("各种排序算法")
        }
    }

    override fun onCategoryClick(data: Category, pos: Int) {
        val intent = Intent(this, AlgorithmDetailActivity::class.java)
        intent.putExtra(AlgorithmDetailActivity.KEY_INDEX, pos)
        intent.putExtra(AlgorithmDetailActivity.KEY_ALGO_TYPE, AlgorithmDetailActivity.TYPE_SORT)
        startActivity(intent)
    }
}