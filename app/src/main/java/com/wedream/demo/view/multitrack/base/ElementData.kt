package com.wedream.demo.view.multitrack.base

abstract class ElementData(private val id: Long,
                           private var start: Int,
                           private var end: Int){

    private var isSelected = false

    fun horizontalMoveBy(deltaX: Int) {
        this.start += deltaX
        this.end += deltaX
    }

    fun setRange(start: Int, end: Int) {
        this.start = start
        this.end = end
    }

    open fun set(other: ElementData) {
        this.start = other.start
        this.end = other.end
    }

    fun getId(): Long {
        return id
    }

    fun getStart(): Int {
        return start
    }

    fun getEnd(): Int {
        return end
    }

    fun isSelect(): Boolean {
        return isSelected
    }

    fun setSelect(select: Boolean) {
        this.isSelected = select
    }

    fun length(): Int {
        return end - start
    }

    abstract  fun <T> copy(): T
}