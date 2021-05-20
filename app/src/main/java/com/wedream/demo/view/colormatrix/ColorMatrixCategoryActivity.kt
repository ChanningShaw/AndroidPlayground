package com.wedream.demo.view.colormatrix

import android.os.Bundle
import com.wedream.demo.app.CategoryActivity
import com.wedream.demo.category.Category
import com.wedream.demo.category.ComponentCategory

class ColorMatrixCategoryActivity : CategoryActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCategoryList(buildData())
    }

    private fun buildData(): List<Category> {
        return listOf(
            ComponentCategory(ColorMatrixActivity::class.java),
            ComponentCategory(ColorHueActivity::class.java),
            ComponentCategory(ColorFilterActivity::class.java)
        )
    }
}