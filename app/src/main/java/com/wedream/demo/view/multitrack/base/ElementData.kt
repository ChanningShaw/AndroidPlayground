package com.wedream.demo.view.multitrack.base

abstract class ElementData(open val id: Long,
                           open var start: Int,
                           open var end: Int){

    var isSelected = false

    open fun updateRange(deltaStart: Int, deltaEnd: Int) {
        this.start += deltaStart
        this.end += deltaEnd
    }

    open fun setRange(start: Int, end: Int) {
        this.start = start
        this.end = end
    }

    open fun set(other: ElementData) {
        this.start = other.start
        this.end = other.end
    }

    abstract  fun <T> copy(): T
}