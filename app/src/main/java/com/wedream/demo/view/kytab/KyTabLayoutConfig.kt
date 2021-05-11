package com.wedream.demo.view.kytab

class KyTabLayoutConfig {
    var tabMargin = 20

    class Build {
        private val config = KyTabLayoutConfig()
        fun tabMargin(tabMargin: Int): Build {
            config.tabMargin = tabMargin
            return this
        }

        fun build(): KyTabLayoutConfig {
            return config
        }
    }
}