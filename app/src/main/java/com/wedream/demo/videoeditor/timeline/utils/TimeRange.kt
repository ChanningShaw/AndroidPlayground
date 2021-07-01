package com.wedream.demo.videoeditor.timeline.utils

class TimeRange(var left: Int, var right: Int) {

    init {
        checkRange()
    }

    fun set(left: Int, right: Int) {
        this.left = left
        this.right = right
        checkRange()
    }

    fun offset(value: Int) {
        left += value
        right += value
    }

    fun overlap(left: Int, right: Int): Boolean {
        return !(right < this.left || left > this.right)
    }

    private fun checkRange() {
        check(left <= right) {
            "right must >= left"
        }
    }

    override fun toString(): String {
        return "[$left,$right]"
    }
}