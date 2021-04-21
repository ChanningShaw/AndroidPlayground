package com.wedream.demo.view

abstract class ScrollRunnable(val scrollMode: ScrollMode,
                              val listener: ScrollListener) : Runnable {
    interface ScrollListener {

        fun onScrolling(offsetX: Int, offsetY: Int)

        fun onScrollEnd()
    }
}


enum class ScrollMode {
    None, ScrollDown, ScrollUp, ScrollLeft, ScrollRight
}