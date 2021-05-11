package com.wedream.demo.view.kytab

import android.widget.LinearLayout
import com.wedream.demo.R

class IndicatorConfig {
    var width = KyTabLayout.DEFAULT_INDICATOR_WIDTH
    var height = LinearLayout.LayoutParams.WRAP_CONTENT
    var bgRes = R.drawable.tab_indicator_bg

    class Build {
        private val config = IndicatorConfig()
        fun width(width: Int): Build {
            config.width = width
            return this
        }

        fun height(height: Int): Build {
            config.height = height
            return this
        }

        fun bgRes(bgRes: Int): Build {
            config.bgRes = bgRes
            return this
        }

        fun build(): IndicatorConfig {
            return config
        }
    }
}