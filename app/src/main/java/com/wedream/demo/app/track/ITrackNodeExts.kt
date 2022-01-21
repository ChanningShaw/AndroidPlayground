package com.ss.android.ugc.aweme.ecommerce.trackimpl

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.app.track.*
import com.wedream.demo.app.track.findParentTrackNode

private const val EXTRA_PREVIOUS_TRACK_NODE_ID = "lib_track_rtn_id"

/**
 * get parent node of a fragment
 */
internal fun Fragment.findParentTrackNode(): ITrackNode? {
    return this.parentFragment as? ITrackNode ?: this.activity as? ITrackNode
}

internal fun getDefaultParentTrackNode(target: Any): ITrackNode? {
    return when (target) {
        is View -> {
            target.findParentTrackNode()
        }
        is RecyclerView.ViewHolder -> {
            target.findParentTrackNode()
        }
        is Fragment -> {
            target.findParentTrackNode()
        }
        else -> null
    }
}

internal fun getDefaultPreviousTrackNode(target: Any): ITrackNode? {
    return when (target) {
        is FragmentActivity -> {
            target.getPreviousTrackNode()
        }
        is Fragment -> {
            target.getPreviousTrackNode()
        }
        is ITrackNode -> {
            // TODO check parentTrackNode
            target.parentTrackNode()?.previousTrackNode()
        }
        else -> null
    }
}

fun Fragment.getPreviousTrackNode(): TrackNodeSnapshot? {
    return this.arguments?.getPreviousTrackNode(this)
        ?: activity?.getPreviousTrackNode()
}

fun FragmentActivity.getPreviousTrackNode(): TrackNodeSnapshot? {
    return this.intent?.getPreviousTrackNode(this)
}

fun Bundle.getPreviousTrackNode(owner: LifecycleOwner): TrackNodeSnapshot? {
    val frozenNodeId = this.getString(EXTRA_PREVIOUS_TRACK_NODE_ID, null)
    return TrackNodeStore.getById(frozenNodeId, owner)
}

fun Intent.getPreviousTrackNode(owner: LifecycleOwner): TrackNodeSnapshot? {
    val frozenNodeId = this.getStringExtra(EXTRA_PREVIOUS_TRACK_NODE_ID)
    return TrackNodeStore.getById(frozenNodeId, owner)
        ?: this.data?.getPreviousTrackNode(owner)
}

fun Uri.getPreviousTrackNode(owner: LifecycleOwner): TrackNodeSnapshot? {
    var frozenNodeId: String? = null
    try {
        frozenNodeId = this.getQueryParameter(EXTRA_PREVIOUS_TRACK_NODE_ID)
    } catch (t: Throwable) {
        // ignore
    }
    return TrackNodeStore.getById(frozenNodeId, owner)
}


fun Intent.setPreviousTrackNode(
    node: ITrackNode,
    updater: TrackParamsUpdater? = null
): TrackNodeSnapshot {
    val frozenNode = TrackNodeStore.snap(node, updater)
    this.putExtra(EXTRA_PREVIOUS_TRACK_NODE_ID, frozenNode.id)
    return frozenNode
}

fun Intent.setPreviousTrackNode(
    view: View,
    updater: TrackParamsUpdater? = null
): TrackNodeSnapshot {
    val frozenNode = TrackNodeStore.snap(view)
    this.putExtra(EXTRA_PREVIOUS_TRACK_NODE_ID, frozenNode.id)
    return frozenNode
}

fun Bundle.setPreviousTrackNode(
    node: ITrackNode,
    updater: TrackParamsUpdater? = null
): TrackNodeSnapshot {
    val frozenNode = TrackNodeStore.snap(node)
    this.putString(EXTRA_PREVIOUS_TRACK_NODE_ID, frozenNode.id)
    return frozenNode
}

fun Bundle.setPreviousTrackNode(
    view: View,
    updater: TrackParamsUpdater? = null
): TrackNodeSnapshot {
    val frozenNode = TrackNodeStore.snap(view)
    this.putString(EXTRA_PREVIOUS_TRACK_NODE_ID, frozenNode.id)
    return frozenNode
}

internal fun RecyclerView.ViewHolder.findParentTrackNode(): ITrackNode? {
    var trackNode = itemView.getParentTrackNode()
    if (trackNode != null && trackNode !== this) {
        return trackNode
    }

    trackNode = itemView.parent as? ITrackNode
        ?: (itemView.parent as? RecyclerView)?.adapter as? ITrackNode
    if (trackNode != null) {
        return trackNode
    }

    return (itemView.parent as? View)?.findParentTrackNode()
}