package com.wedream.demo.view.multitrack.base

import android.database.Observable
import android.view.ViewGroup
import com.wedream.demo.view.multitrack.PlaneRecycler

abstract class AbsPlaneRecyclerAdapter<H : PlaneRecycler.ViewHolder> {

    private val observable = AdapterDataObservable()

    abstract fun getElementIds(): List<Long>

    abstract fun getViewBorder(id: Long, parent: ViewGroup): PlaneRecycler.ViewBorder

    abstract fun onCreateElementHolder(parent: ViewGroup, viewType: Int): H

    abstract fun onBindElementHolder(holder: H, id: Long)

    open fun getElementType(id: Long): Int {
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

    fun notifyItemChanged(id: Long) {
        observable.notifyItemChanged(id)
    }

    fun notifyItemMoved(id: Long) {
        observable.notifyItemMoved(id)
    }

    fun notifyItemInserted(id: Long) {
        observable.notifyItemInserted(id)
    }

    fun notifyItemRemoved(id: Long) {
        observable.notifyItemRemoved(id)
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

        fun notifyItemChanged(id: Long) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onItemChanged(listOf(id))
            }
        }

        fun notifyItemMoved(id: Long) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onItemMoved(listOf(id))
            }
        }

        fun notifyItemInserted(id: Long) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onItemInserted(listOf(id))
            }
        }

        fun notifyItemRemoved(id: Long) {
            for (i in mObservers.indices.reversed()) {
                mObservers[i].onItemRemoved(listOf(id))
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

        open fun onItemChanged(ids: List<Long>) {

        }

        open fun onItemMoved(ids: List<Long>) {

        }

        open fun onItemInserted(ids: List<Long>) {
        }

        open fun onItemRemoved(ids: List<Long>) {
        }

        open fun handleHorizontalTouchEvent(handle: Boolean) {

        }
    }
}