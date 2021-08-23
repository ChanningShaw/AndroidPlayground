package com.wedream.demo.algo.model.sort

import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option

/**
 * 排序的代理类
 */
class SortProxy<T : Comparable<T>> : SortModel<T>() {

    private var currentModel: SortModel<*> = SelectSort<T>()

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
            Option(SortType.Merge.ordinal, "归并排序"),
            Option(SortType.Quick.ordinal, "快速排序"),
            Option(SortType.Shell.ordinal, "希尔排序"),
            Option(SortType.Counting.ordinal, "计数排序"),
        )
    }

    override fun onOptionSelect(option: Option) {
        when (option.id) {
            SortType.Select.ordinal -> {
                currentModel = SelectSort<T>()
            }
            SortType.Bubble.ordinal -> {
                currentModel = BubbleSort<T>()
            }
            SortType.Heap.ordinal -> {
                currentModel = HeapSort<T>()
            }
            SortType.Insert.ordinal -> {
                currentModel = InsertSort<T>()
            }
            SortType.Merge.ordinal -> {
                currentModel = MergeSort<T>()
            }
            SortType.Quick.ordinal -> {
                currentModel = QuickSort<T>()
            }
            SortType.Shell.ordinal -> {
                currentModel = ShellSort<T>()
            }
            SortType.Counting.ordinal -> {
                currentModel = CountingSort()
            }
        }
    }

    override fun getSortProperty(): SortProperty {
        return currentModel.getSortProperty()
    }

    override fun onSort() {

    }
}