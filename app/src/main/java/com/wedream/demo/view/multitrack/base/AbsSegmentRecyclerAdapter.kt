package com.wedream.demo.view.multitrack.base

import android.database.Observable
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class AbsSegmentRecyclerAdapter<H : AbsSegmentRecyclerAdapter.ViewHolder> {

    private val observable = AdapterDataObservable()

    abstract fun onCreateTrackHolder(parent: ViewGroup, trackType: Int): H

    abstract fun onCreateSegmentHolder(parent: ViewGroup, viewType: Int): H

    abstract fun onBindTrackHolder(holder: H, trackLevel: Int)

    abstract fun onBindSegmentHolder(holder: H, segmentId: Long)

    abstract fun getTrackLevels(): List<Int>

    abstract fun getSegmentIds(): List<Long>

    open fun getTrackType(trackLevel: Int): Int {
        return 0
    }

    open fun getSegmentType(segmentId: Long): Int {
        return 0
    }

    open fun registerAdapterDataObserver(observer: AdapterDataObserver) {
        observable.registerObserver(observer)
    }

    open fun unregisterAdapterDataObserver(observer: AdapterDataObserver) {
        observable.unregisterObserver(observer)
    }

    fun notifyDataSetChanged() {
        observable.notifyChanged()
    }

    fun notifyTrackInsert(trackLevel: Int) {
        observable.notifyTrackInsert(trackLevel)
    }

    fun notifyItemChanged(segmentId: Long) {
        observable.notifyItemChanged(segmentId)
    }


    fun handleHorizontalTouchEvent(handle: Boolean) {
        observable.handleHorizontalTouchEvent(handle)
    }

    private class AdapterDataObservable : Observable<AdapterDataObserver>() {
        fun notifyChanged() {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onChanged()
            }
        }

        fun notifyTrackInsert(trackLevel: Int) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onTrackInserted(listOf(trackLevel))
            }
        }

        fun notifyItemChanged(id: Long) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onItemChanged(listOf(id))
            }
        }

        fun handleHorizontalTouchEvent(handle: Boolean) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].handleHorizontalTouchEvent(handle)
            }
        }
    }

    abstract class AdapterDataObserver {
        open fun onChanged() {
        }


        open fun onTrackInserted(trackLevels: List<Int>) {

        }

        open fun onTrackRemoved(trackLevels: List<Int>) {

        }

        open fun onItemChanged(ids: List<Long>) {

        }

        open fun onItemInserted(ids: List<Long>) {
        }

        open fun onItemRemoved(ids: List<Long>) {
        }

        open fun handleHorizontalTouchEvent(handle: Boolean) {

        }
    }

    abstract class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var x = 0
        var y = 0
        var width = 0
        var height = 0
    }
}