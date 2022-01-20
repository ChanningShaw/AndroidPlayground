package com.wedream.demo.app.track

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.wedream.demo.R

private const val EXTRA_REFERRER_TRACK_NODE_ID = "lib_track_rtn_id"
private const val TAG_ID_PARENT_TRACK_NODE = R.id.lib_track_tag_parent_track_node
private const val TAG_ID_PARAMS_SOURCE = R.id.lib_track_tag_id_params_source


/**
 * 获取View关联的最近的TrackNode对象
 * 1. 如果View本身实现了ITrackNode则返回自身
 * 2. 从[View.findParentTrackNode]获取最近的父节点
 */
val View.trackNode: ITrackNode?
    get() {
        return this as? ITrackNode ?: findParentTrackNode()
    }

val View.referrerTrackNode: ITrackNode?
    get() {
        return this.trackNode?.previousTrackNode()
    }

/**
 * 删除Bundle中的来源节点
 */
fun Bundle.clearReferrerTrackNode() {
    this.remove(EXTRA_REFERRER_TRACK_NODE_ID)
}

/**
 * 删除Intent中的来源节点
 */
fun Intent.clearReferrerTrackNode() {
    this.removeExtra(EXTRA_REFERRER_TRACK_NODE_ID)
}