package com.wedream.demo.view

import android.os.Bundle
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.view.kytab.IndicatorConfig
import com.wedream.demo.view.kytab.KyTabLayout


class TabLayoutActivity : BaseActivity() {

    private val tabs = arrayOf("AAAAAAAAAAAAAAAAA", "B", "C", "D")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)
        val tabLayout: KyTabLayout = findViewById(R.id.tab)
        for (t in tabs) {
            val tab = tabLayout.newTab(t)
            tabLayout.addTab(tab)
        }
        val tab = tabLayout.ImageTab(R.drawable.mat_image)
        tabLayout.setIndicatorConfig(IndicatorConfig.Build().width(50).build())
        tabLayout.addTab(tab)
        for (i in 1..20) {
            val tab = tabLayout.newTab(i.toString())
            tabLayout.addTab(tab)
        }

        tabLayout.setOnTabClickListener(object : KyTabLayout.OnTabClickListener {
            override fun onTabClick(tab: KyTabLayout.Tab, index: Int) {
                if (index == 4) {
                    val tab = tabLayout.newTab("E")
                    tabLayout.replaceTab(tab, index)
                }
            }
        })
    }
}