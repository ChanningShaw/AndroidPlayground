package com.wedream.demo.app.track

import android.util.Log
import android.view.View

internal fun ITrackNode.fillChain(trackParams: TrackParams) {
    var currNode: ITrackNode? = this
    while (currNode != null) {

        withDebugFill(trackParams){
            // fill current node
            (currNode as? IPageTrackNode)?.let {
                trackParams[it.getPageNameKey()] = it.getPageName()
            }
             // TODO 遍历的顺序
            currNode?.fillTrackParams(trackParams)
            currNode?.javaClass?.simpleName?:""
        }

        if (currNode is IPageTrackNode) {
            mapPreviousParams(currNode, trackParams)
        }

        // try to fill parent node
        currNode = currNode.parentTrackNode()
    }
}

internal fun View.fillChain(trackParams: TrackParams) {
    var currView: View? = this
    var currNode: ITrackNode?
    while (currView != null) {
        withDebugFill(trackParams) {
            // fill params from current view
            currView?.paramsSource?.fillTrackParams(trackParams)
            currView?.javaClass?.simpleName ?: ""
        }
        // if current view impl ITrackNode or has set parentTrackNode, switch to track node chain
        currNode = currView as? ITrackNode ?: currView.getParentTrackNode()
        if (currNode != null) {
            currNode.fillChain(trackParams)
            return
        }

        // find parent view through view tree
        currView = currView.parent as? View
    }

    // if we reach here, it means that all we passed is in view tree, not track node chain, try context finally
    context?.trackNode?.fillChain(trackParams)
}

internal fun mapPreviousParams(page: IPageTrackNode, trackParams: TrackParams) {
    val previousNode = page.previousTrackNode() ?: return
    val previousParams = TrackParams()
    previousNode.fillChain(previousParams)
    applyMapRule(previousParams, page.getMapRule())
    trackParams.putAll(previousParams)
}

private fun applyMapRule(previousParams: TrackParams, mapRule: Map<String, String>) {
    for (e in mapRule) {
        previousParams.remove(e.key)?.let {
            previousParams[e.value] = it
        }
    }
}

fun withDebugFill(trackParams: TrackParams, callback: () -> String) {
    val isDebug = true
    var debugTrackParam: TrackParams? = null
    if (isDebug && trackParams.isNotEmpty()) {
        debugTrackParam = TrackParams()
        debugTrackParam.putAll(trackParams)
    }
    val debugTag = callback.invoke()

    if (isDebug) {
        trackParams.forEach { entry ->
            if (debugTrackParam?.contains(entry.key) == false) {
                Log.e(debugTag, "key: ${entry.key}  value: ${entry.value}\n")
            }
        }
    }
}