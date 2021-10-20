package com.wedream.demo.lifecycle

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.util.LogUtils.log

@Deprecated("生命周期还不完善")
open class LifecycleHolder(itemView: View) : RecyclerView.ViewHolder(itemView), LifecycleOwner {
    init {
        itemView.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
            // View onDetached 的时候回调 onDestroy()
            override fun onViewDetachedFromWindow(v: View?) {
                itemView.removeOnAttachStateChangeListener(this)
                log { "LifecycleViewHolder ${this@LifecycleHolder} onDestroy" }
                onDestroy()
            }

            // View onAttached 的时候回调 onCreate()
            override fun onViewAttachedToWindow(v: View?) {
                log { "LifecycleViewHolder ${this@LifecycleHolder} onCreate" }
                onCreate()
            }
        })
    }

    private val lifecycleRegistry: LifecycleRegistry by lazy { LifecycleRegistry(this) }

    open fun onCreate() {
        lifecycleRegistry.currentState = Lifecycle.State.CREATED
        lifecycleRegistry.currentState = Lifecycle.State.STARTED
    }

    open fun onResume() {
        lifecycleRegistry.currentState = Lifecycle.State.RESUMED
    }

    open fun onDestroy() {
        lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}