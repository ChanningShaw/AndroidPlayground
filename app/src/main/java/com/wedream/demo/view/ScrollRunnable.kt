package com.wedream.demo.view

import android.view.View

abstract class ScrollRunnable(val targetView: View,
                              val info: CrossTrackMovementActivity.ViewInfo,
                              val scrollMode: ScrollMode,
                              val listener: ScrollListener) : Runnable {
    interface ScrollListener {
        fun onScrollEnd()
    }
}


enum class ScrollMode {
    None, ScrollDown, ScrollUp, ScrollLeft, ScrollRight
}