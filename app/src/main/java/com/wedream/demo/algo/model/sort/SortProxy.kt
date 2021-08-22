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
        get() = currentModel.tips + "\n${currentModel.getSortProperty()}"

    override fun execute(option: Option?): ExecuteResult {
        return currentModel.execute(option)
    }

    override fun getOptions(): List<Option> {
        return listOf(
            Option(SortType.Select.ordinal, "选择排序"),
            Option(SortType.Bubble.ordinal, "冒泡排序"),
            Option(SortType.Heap.ordinal, "堆排序"),
            Option(SortType.Insert.ordinal, "插入排序"),
        )
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
            SortType.Insert.ordinal -> {
                currentModel = InsertSort()
            }
        }
    }

    override fun getSortProperty(): SortProperty {
        return currentModel.getSortProperty()
    }

    override fun onSort() {

    }
}