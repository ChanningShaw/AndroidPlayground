package com.wedream.demo.view.colormatrix

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R


class ColorFilterActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private var filtersAdapter: FiltersAdapter? = null
    private val filters: MutableList<FloatArray> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_color_filter)
        recyclerView = findViewById<View>(R.id.recyclerView) as RecyclerView
        inItFilters()
        filtersAdapter = FiltersAdapter(layoutInflater, filters)
        val gridLayoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.adapter = filtersAdapter
    }

    private fun inItFilters() {
        filters.add(ColorFilter.colormatrix_heibai)
        filters.add(ColorFilter.colormatrix_fugu)
        filters.add(ColorFilter.colormatrix_gete)
        filters.add(ColorFilter.colormatrix_chuan_tong)
        filters.add(ColorFilter.colormatrix_danya)
        filters.add(ColorFilter.colormatrix_guangyun)
        filters.add(ColorFilter.colormatrix_fanse)
        filters.add(ColorFilter.colormatrix_hepian)
        filters.add(ColorFilter.colormatrix_huajiu)
        filters.add(ColorFilter.colormatrix_jiao_pian)
        filters.add(ColorFilter.colormatrix_landiao)
        filters.add(ColorFilter.colormatrix_langman)
        filters.add(ColorFilter.colormatrix_ruise)
        filters.add(ColorFilter.colormatrix_menghuan)
        filters.add(ColorFilter.colormatrix_qingning)
        filters.add(ColorFilter.colormatrix_yese)
    }
}