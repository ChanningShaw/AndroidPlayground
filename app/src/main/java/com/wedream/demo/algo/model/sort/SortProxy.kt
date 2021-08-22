package com.wedream.demo.algo.model.sort

import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option

/**
 * 排序的代理类
 */
class SortProxy<T : Comparable<T>> : SortModel<T>() {

    private var currentModel: SortModel<T> = SelectSort()

    override val title: String
        get() = currentModel.title

    override val tips: String
        get() = currentModel.tips

    override fun execute(option: Option?): ExecuteResult {
        return currentModel.execute(option)
    }

    override fun onOptionSelect(option: Option) {
        when (option.id) {
            SortType.Select.ordinal -> {
                currentModel = SelectSort()
            }
            SortType.Bubble.ordinal -> {
                currentModel = BubbleSort()
            }
            SortType.Heap.ordinal -> {
                currentModel = HeapSort()
            }
        }
    }

    override fun getSortProperty(): SortProperty {
        return currentModel.getSortProperty()
    }

    override fun onSort() {

    }
}