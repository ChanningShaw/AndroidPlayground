package com.wedream.demo.view

abstract class ScrollRunnable(val scrollMode: ScrollMode,
                              val listener: ScrollListener) : Runnable {
    interface ScrollListener {

        fun onScrolling(offsetX: Int, offsetY: Int)

        fun onScrollEnd()
    }
}


enum class ScrollMode {
    None, Pending, ScrollDown, ScrollUp, ScrollLeft, ScrollRight
}

fun isScrolling(mode: ScrollMode) : Boolean {
    return mode == ScrollMode.ScrollUp
            || mode == ScrollMode.ScrollDown
            || mode == ScrollMode.ScrollLeft
            || mode == ScrollMode.ScrollRight
}