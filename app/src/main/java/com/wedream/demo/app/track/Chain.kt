package com.wedream.demo.app.track

import android.view.View

internal fun ITrackNode.fillChain(trackParams: TrackParams) {
    var currNode: ITrackNode? = this
    while (currNode != null) {
        // 填充当前节点的参数
        currNode.fillTrackParams(trackParams)

        // 沿着责任链找到父级节点
        currNode = currNode.parentTrackNode()
    }
}

internal fun View.fillChain(trackParams: TrackParams) {
    var currView: View? = this
    var currNode: ITrackNode?
    while (currView != null) {
        // 填充当前view的参数
        currView.paramsSource?.fillTrackParams(trackParams)

        // 如果当前view实现了ITrackNode，或者设置了parentTrackNode，则切换到ITrackNode链路
        currNode = currView as? ITrackNode ?: currView.getParentTrackNode()
        if (currNode != null) {
            currNode.fillTrackParams(trackParams)
            return
        }

        // 沿着ViewTree找到父View
        currView = currView.parent as? View
    }

    // 如果走到这里，说明中间都是通过ViewTree，没有切换到ITrackNode链路，最后尝试下context
    context?.trackNode?.fillChain(trackParams)
}