package com.wedream.demo.app.track

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import androidx.fragment.app.Fragment
import com.wedream.demo.R

private const val TAG_ID_PARENT_TRACK_NODE = R.id.lib_track_tag_parent_track_node
private const val TAG_ID_PARAMS_SOURCE = R.id.lib_track_tag_id_params_source

fun View.logWhenShow(eventName: String, updater: TrackParamsUpdater? = null) {
    setupTrack {
        logEvent(eventName, updater)
    }
}

fun View.logEvent(eventName: String, updater: TrackParamsUpdater? = null) {
    Event(eventName).logView(this, updater)
}

fun logStart(eventName: String, updater: TrackParamsUpdater? = null) {
    ContinuousEvent(eventName).apply {
        update(updater)
        start()
    }
}

fun ITrackNode.logEnd(eventName: String, updater: ((TrackParams, Long) -> Unit)? = null) {
    ContinuousEvent.get(eventName)?.end(this, updater)
}

fun ITrackNode.logEvent(eventName: String, updater: TrackParamsUpdater? = null) {
    Event(eventName).logNode(this, updater)
}

/**
 * 获取Fragment的父节点
 */
internal fun Fragment.findParentTrackNode(): IParamsSource? {
    return this.parentFragment as? IParamsSource
        ?: this.activity as? IParamsSource
}

/**
 * 默认实现任意节点的来源节点
 */
//internal fun getDefaultReferrerTrackNode(target: Any): ITrackNode? {
//    return when (target) {
//        is Activity -> {
//            target.getReferrerTrackNode()
//        }
//        is Fragment -> {
//            target.getReferrerTrackNode()
//        }
//        is ITrackNode -> {
//            target.parentTrackNode()
//                ?.referrerTrackNode()
//        }
//        else -> null
//    }
//}

/**
 * 获取View的父节点，沿着ViewTree向上寻找
 */
internal fun View.findParentTrackNode(): IParamsSource? {
    var trackNode: IParamsSource?
    var currView: View? = this

    while (currView != null) {
        // 先看看是否手动设置过view的父节点
        trackNode = currView.getParentTrackNode()
        if (trackNode != null) {
            return trackNode
        }

        // 当前位置移到ViewTree的父节点
        currView = currView.parent as? View
        // 看看父节点是不是ITrackNode
        if (currView is IParamsSource) {
            return currView
        }
    }

    return this.context?.trackNode
}

/**
 * 获取View设置的父节点
 */
fun View.getParentTrackNode(): IPageTrackNode? {
    return this.getTag(TAG_ID_PARENT_TRACK_NODE) as? IPageTrackNode
}

/**
 * 设置View的TrackModel
 */
var View.paramsSource: IParamsSource?
    get() = this.getTag(TAG_ID_PARAMS_SOURCE) as? IParamsSource
    set(value) {
        this.setTag(TAG_ID_PARAMS_SOURCE, value)
    }


/**
 * 尝试将Activity用作一个ITrackNode
 */
val Activity.trackNode: ITrackNode?
    get() {
        return this as? ITrackNode
    }

val Context.trackNode: ITrackNode?
    get() {
        if (this is ITrackNode) {
            return this
        } else if (this is ContextWrapper && this.baseContext is ITrackNode) {
            return this.baseContext as ITrackNode
        }
        return null
    }

