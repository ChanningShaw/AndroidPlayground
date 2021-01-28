package com.wedream.demo.view.multitrack.base

import android.database.Observable
import android.view.View
import android.view.ViewGroup

abstract class AbsPlaneRecyclerAdapter<H : AbsPlaneRecyclerAdapter.ViewHolder> {

    private val observable = AdapterDataObservable()

    abstract fun onCreateElementHolder(parent: ViewGroup, viewType: Int): H

    abstract fun onBindElementHolder(holder: H, id: Long)

    abstract fun getElementIds(): List<Long>

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

        open fun onItemInserted(ids: List<Long>) {
        }

        open fun onItemRemoved(ids: List<Long>) {
        }

        open fun handleHorizontalTouchEvent(handle: Boolean) {

        }
    }

    abstract class ViewHolder(val itemView: View){
        var x = 0
        var y = 0
        var width = 0
        var height = 0

        open fun bindData(){

        }

        override fun toString(): String {
            return "ViewHolder: x = $x, y = $y, width = $width, height = $height"
        }
    }
}