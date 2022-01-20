package com.wedream.demo.app.track

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

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
    fun snap(trackNode: ITrackNode): TrackNodeSnapshot {
        if (trackNode is TrackNodeSnapshot) return trackNode

        val fnId = generateFrozenNodeId(trackNode)

        return TrackNodeSnapshot(
            fnId,
            TrackParams().also { params ->
                trackNode.fillChain(params)
            },
            trackNode.previousTrackNode() as? TrackNodeSnapshot
        ).apply {
            id2FrozenNodes[fnId] = this
        }
    }

    @JvmStatic
    fun snap(view: View): TrackNodeSnapshot {
        val fnId = generateFrozenNodeId(view)
        val previousNode = view.trackNode?.previousTrackNode()
        return TrackNodeSnapshot(
            fnId,
            TrackParams().also { params ->
                previousNode?.fillChain(params)
            },
            view.referrerTrackNode as? TrackNodeSnapshot
        ).apply {
            id2FrozenNodes[fnId] = this
        }
    }

    @JvmStatic
    fun getById(id: String?, owner: LifecycleOwner): TrackNodeSnapshot? {
        return id2FrozenNodes[id ?: return null].apply {
            addLifecycleObserver(owner, id)
        }
    }

    @JvmStatic
    fun removeSnapshot(node: TrackNodeSnapshot?) {
        node?.let {
            id2FrozenNodes.remove(it.id)
            val iterator = owner2Id.iterator()
            while (iterator.hasNext()) {
                val e = iterator.next()
                if (e.value == it.id) {
                    iterator.remove()
                    break
                }
            }
        }
    }

    @JvmStatic
    fun count(): Int {
        return id2FrozenNodes.size
    }

    private fun addLifecycleObserver(owner: LifecycleOwner, nodeId: String) {
        owner.lifecycle.addObserver(observer)
        owner2Id[owner] = nodeId
    }

    private fun removeLifecycleObserver(source: LifecycleOwner) {
        owner2Id[source]?.let {
            id2FrozenNodes.remove(it)
        }
        owner2Id.remove(source)
        source.lifecycle.removeObserver(observer)
    }
}