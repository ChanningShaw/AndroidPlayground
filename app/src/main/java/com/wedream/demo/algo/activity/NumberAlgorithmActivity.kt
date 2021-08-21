package com.wedream.demo.algo.activity

import android.content.Intent
import android.os.Bundle
import com.wedream.demo.algo.algo.*
import com.wedream.demo.app.CategoryActivity
import com.wedream.demo.category.Category

class NumberAlgorithmActivity : CategoryActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCategoryList(buildData())
    }

    private fun buildData(): List<Category> {
        return NumberAlgorithm.getModels().map {
            Category(it.name)
        }
    }

    override fun onCategoryClick(data: Category, pos: Int) {
        val intent = Intent(this, AlgorithmDetailActivity::class.java)
        intent.putExtra(AlgorithmDetailActivity.KEY_INDEX, pos)
        intent.putExtra(AlgorithmDetailActivity.KEY_ALGO_TYPE, AlgorithmDetailActivity.TYPE_NUMBER)
        startActivity(intent)
    }
}