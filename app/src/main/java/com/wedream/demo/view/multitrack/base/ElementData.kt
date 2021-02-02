package com.wedream.demo.view.multitrack.base

import android.graphics.Rect

abstract class ElementData(val id: Long){

     var left: Int = 0
     var top: Int = 0
     var width: Int = 0
     var height: Int = 0

    private var isSelected = false

    constructor(id: Long, left: Int, top: Int, width: Int, height: Int) : this(id) {
        this.left = left
        this.top = top
        this.width = width
        this.height = height
    }

    constructor(id: Long, rect: Rect) : this(id, rect.left, rect.top, rect.width(), rect.height())

    fun horizontalMoveBy(offset: Int) {
        this.left += offset
    }

    open fun set(other: ElementData) {
        this.left = other.left
        this.top = other.top
        this.width = other.width
        this.height = other.height
    }

    fun isSelect(): Boolean {
        return isSelected
    }

    fun setSelect(select: Boolean) {
        this.isSelected = select
    }

    fun right(): Int {
        return left + width
    }

    fun bottom(): Int {
        return top + height
    }

    abstract  fun <T> copy(): T
}