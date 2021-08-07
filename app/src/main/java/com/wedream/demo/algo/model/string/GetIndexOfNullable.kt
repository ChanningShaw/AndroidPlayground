package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string
import java.lang.StringBuilder

class GetIndexOfNullable : AlgorithmModel() {

    override var name = "在有序但含有空的字符串数组中查找字符串"

    override var title = "给定一个字符串数组strs[]，在strs中有些位置为null，但在不为null的位置上，" +
            "其字符串是按照字典顺序从小到大的，在给定一个字符串str，请返回str在strs中出现的最左的位置。"

    override var tips = "二分查找，如果找到的为空，那么先往左遍历，如果左边没有就往右找。"

    override fun execute(option: Option?): ExecuteResult {
        val strs = arrayOf(
            "a", null, "b", "b", null, null, null, "c", "c"
        )
        val str = "c"
        val output = getIndex(strs, str)
        return ExecuteResult(strs.string(), output.string())
    }

    companion object {
        fun getIndex(strs: Array<String?>, str: String): Int {
            if (strs.isEmpty()) {
                return -1
            }
            var res = -1
            var left = 0
            var right = strs.lastIndex
            while (left <= right) {
                val mid = (left + right) / 2
                if (strs[mid] == str) {
                    // 找到目标串，继续往左找
                    res = mid
                    right = mid - 1
                } else if (strs[mid] != null) {
                    if (strs[mid]!! < str) {
                        // 小于目标串，在右边找
                        left = mid + 1
                    } else {
                        // 大于目标串，在左边找
                        right = mid - 1
                    }
                } else {
                    var i = mid
                    while (strs[i] == null && --i >= left) {}
                    if (i < left || strs[i]!! < str) {
                        // 左边都为空串，在右边继续找
                        left = mid + 1
                    } else {
                        // 左边找到不为空的串
                        if (strs[i] == str) {
                            res = i
                        }
                        right = i - 1
                    }
                }
            }
            return res
        }
    }
}