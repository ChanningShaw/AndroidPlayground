package com.wedream.demo.app.track

import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.os.Build
import android.os.SystemClock
import android.view.View
import android.view.ViewTreeObserver
import android.view.WindowManager
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import kotlinx.coroutines.*

private const val INTERVAL = 100L

class ViewTracker(
    private val targetView: View,
    private var onShow: (View) -> Unit
) {

    private var rect = Rect()
    private var screenWidth = 0
    private var screenHeight = 0
    private var lastCheckTime = 0L
    private var lastVisible = false
    private var stopped = false

    @Volatile
    private var job: Job? = null

    private var onDrawListener = object : ViewTreeObserver.OnDrawListener {
        override fun onDraw() {
            if (stopped) {
                return
            }
            log { "view ${targetView.tag} onDraw, listener = $this" }
            if (job != null) {
                return
            }
            val currentTime = SystemClock.elapsedRealtime()
            if (currentTime - lastCheckTime < INTERVAL) {
                job = CoroutineScope(Dispatchers.Main).launch {
                    delay(lastCheckTime + INTERVAL - currentTime)
                    checkVisible()
                    job = null
                }
                log { "create job1 = $job for view ${targetView.tag}" }
            } else {
                job = CoroutineScope(Dispatchers.Main).launch {
                    checkVisible()
                    job = null
                }
                log { "create job2 = $job for view ${targetView.tag}" }
            }
        }
    }

    private var onAttachStateChangeListener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View) {
            stopped = false
        }

        override fun onViewDetachedFromWindow(v: View) {
            job?.cancel()
            job = null
            stopped = true
        }
    }

    private suspend fun checkVisible() {
        lastCheckTime = SystemClock.elapsedRealtime()
        targetView.getGlobalVisibleRect(rect)
        log { "view ${targetView.tag} isShown = ${targetView.isShown}" }
        var visible = false
        if (!targetView.isShown) {
            visible = false
        } else {
            val centerX = rect.centerX()
            val centerY = rect.centerY()
            log { "view ${targetView.tag} rect = $rect" }
            if (centerX > 0 && centerX < screenWidth && centerY > 0 && centerY < screenHeight) {
                visible = true
            }
        }

        if (visible && !lastVisible && !stopped) {
            log { "view ${targetView.tag} become visible, this = ${this@ViewTracker}, job = $job" }
            onShow.invoke(targetView)
        }
        log { "view ${targetView.tag} visible = $visible lastVisible = $lastVisible" }
        lastVisible = visible
    }

    fun start() {
        updateScreenSize(targetView.context)
        targetView.viewTreeObserver.addOnDrawListener(onDrawListener)
        targetView.addOnAttachStateChangeListener(onAttachStateChangeListener)
        stopped = false
    }

    private fun updateScreenSize(context: Context) {
        try {
            val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            val point = Point()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                // 全面屏需要使用getRealSize来获取高度
                wm.defaultDisplay.getRealSize(point)
                if (point.y > 0 && point.x > 0) {
                    screenWidth = point.x
                    screenHeight = point.y
                    return
                }
            }
        } catch (ignored: Exception) {
        }
        screenWidth = context.resources.displayMetrics.widthPixels
        screenHeight = context.resources.displayMetrics.heightPixels
    }

    internal fun reset(onShowCallback: (View) -> Unit) {
        job?.cancel()
        job = null
        onShow = onShowCallback
        lastVisible = false
        log { "reset view ${targetView.tag}, tracker = $this" }
    }
}

internal fun View.setupTrack(onShow: (View) -> Unit) {
    val oldTracker = getTag(R.id.lib_track_tag_id_view_tracker) as? ViewTracker
    if (oldTracker != null) {
        oldTracker.reset(onShow)
    } else {
        val tracker = ViewTracker(this, onShow)
        setTag(R.id.lib_track_tag_id_view_tracker, tracker)
        log { "start view ${this.tag}, tracker = $tracker" }
        tracker.start()
    }
}