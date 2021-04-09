package com.wedream.demo.algo.activity

import android.os.Bundle
import com.wedream.demo.algo.algo.ArrayAlgorithm
import com.wedream.demo.app.CategoryActivity
import com.wedream.demo.category.Category
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.print

class ArrayAlgorithmActivity() : CategoryActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCategoryList(buildData())
    }

    private fun buildData(): List<Category> {
        return listOf(
            Category("生成窗口的最大数组值"),
            Category("最大值减最小值小于或等于num的子数组数量")
        )
    }

    override fun onCategoryClick(data: Category, pos: Int) {
        when (pos) {
            0 -> {
                val input = intArrayOf(4, 3, 5, 4, 3, 3, 6, 7)
                val result = ArrayAlgorithm.maxValuesInSlidingWindow(input, 3)
                result.print()
            }
            1 -> {
                val input = intArrayOf(6, 2, 5, 3, 4, 1)
                val num = ArrayAlgorithm.subArrayCountWithValueInRange(input, 3)
                log { num }
            }
        }
    }
}