package com.wedream.demo.app.track

import android.view.View
import com.wedream.demo.util.LogUtils.log

open class Event(var name: String) {
    val params = TrackParams()
    var chainNode: ITrackNode? = null
    var chainView: View? = null

    open fun put(key: String, value: Any?): Event {
        params[key] = value
        return this
    }

    open fun put(map: Map<String, Any>): Event {
        params.putAll(map)
        return this
    }

    open fun node(node: ITrackNode): Event {
        this.chainNode = node
        return this
    }

    open fun node(view: View): Event {
        this.chainView = view
        return this
    }

    open fun update(updater: TrackParamsUpdater?): Event {
        updater?.invoke(params)
        return this
    }

    open fun logView(view: View, updater: TrackParamsUpdater?) {
        update(updater).node(view).emit()
    }

    open fun logNode(node: ITrackNode, updater: TrackParamsUpdater?) {
        update(updater).node(node).emit()
    }

//    open fun chainBy(fragment: Fragment): Event {
//        this.chainNode = fragment.trackNode
//        return this
//    }
//
//    open fun chainBy(activity: Activity): Event {
//        this.chainNode = activity.trackNode
//        return this
//    }

    open fun emit() {
        chainNode?.fillChain(params)
        if (chainNode is IPageTrackNode) {
            params[Events.Key.PAGE_NAME] = (chainNode as IPageTrackNode).getPageName()
        }
        chainView?.fillChain(params)
        log { "$name : $params"  }
    }
}