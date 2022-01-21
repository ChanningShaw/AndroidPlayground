package com.wedream.demo.app.track

import android.view.View
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object TrackNodeStore {

    private val id2FrozenNodes = HashMap<String, TrackNodeSnapshot>()
    private val owner2Id = HashMap<LifecycleOwner, String>()

    var recent: ITrackNode? = null
        get() {
            val temp = field
            field = null
            return temp
        }
        set(value) {
            field = value?.let { snap(it) }
        }

    private val observer: LifecycleObserver = LifecycleEventObserver { source, event ->
        if (event == Lifecycle.Event.ON_DESTROY) {
            removeLifecycleObserver(source)
        }
    }

    private fun generateFrozenNodeId(node: Any): String {
        return "fn_${node.javaClass.simpleName}__${node.hashCode()}__${System.currentTimeMillis()}"
    }

    @JvmStatic
    fun snap(
        trackNode: ITrackNode,
        updater: TrackParamsUpdater? = null
    ): TrackNodeSnapshot {
        if (trackNode is TrackNodeSnapshot) return trackNode

        val fnId = generateFrozenNodeId(trackNode)

        return TrackNodeSnapshot(
            fnId,
            TrackParams().also { params ->
                trackNode.fillChain(params)
                updater?.invoke(params)
            },
            trackNode.previousTrackNode() as? TrackNodeSnapshot
        ).apply {
            id2FrozenNodes[fnId] = this
        }
    }

    @JvmStatic
    fun snap(
        view: View,
        updater: TrackParamsUpdater? = null
    ): TrackNodeSnapshot {
        val fnId = generateFrozenNodeId(view)
        val previousNode = view.trackNode?.previousTrackNode()
        return TrackNodeSnapshot(
            fnId,
            TrackParams().also { params ->
                previousNode?.fillChain(params)
                updater?.invoke(params)
            },
            view.referrerTrackNode as? TrackNodeSnapshot
        ).apply {
            id2FrozenNodes[fnId] = this
        }
    }

    @JvmStatic
    fun getById(id: String?, owner: LifecycleOwner): TrackNodeSnapshot? {
        return id2FrozenNodes[id ?: return null].apply {
            owner.lifecycle.coroutineScope.launch(Dispatchers.Main) {
                addLifecycleObserver(owner, id)
            }
        }
    }

    private fun addLifecycleObserver(owner: LifecycleOwner, nodeId: String) {
        if (owner2Id[owner] == null) {
            owner.lifecycle.addObserver(observer)
            owner2Id[owner] = nodeId
        }
    }

    private fun removeLifecycleObserver(source: LifecycleOwner) {
        owner2Id[source]?.let {
            id2FrozenNodes.remove(it)
        }
        owner2Id.remove(source)
        source.lifecycle.removeObserver(observer)
    }
}