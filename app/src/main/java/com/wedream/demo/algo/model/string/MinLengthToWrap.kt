package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class MinLengthToWrap : AlgorithmModel() {

    override var name = "最小包含子串的长度"

    override var title = "给定字符串str1和str2，求str1的子串中含有str2所有字符的子串的最小长度。" +
            "例如str1='abcde', str2='ac', 子串abc满足条件，返回3"

    override var tips = "滑动窗口模式。滑动窗口还没有包含str2的全部字符时，移动right扩大滑动窗口的范围。" +
            "当滑动窗口已经包含str2的全部字符时，移动left缩小滑动窗口的范围求最小长度。"

    override fun execute(option: Option?): ExecuteResult {
        val str1 = "adabbca"
        val str2 = "acb"
        val output = minLengthToWrap(str1, str2)
        return ExecuteResult("$str1, $str2", output.string())
    }

    companion object {
        fun minLengthToWrap(str1: String, str2: String):Int {
            if (str1.isEmpty() || str2.isEmpty() || str2.length > str1.length) {
                return 0
            }
            val map = IntArray(256) // 各个字符还差的数量
            for (c in str2) {
                map[c.code]++
            }
            var left = 0
            var right = 0
            var count = str2.length // 还差多个字符才够
            var minLength = Int.MAX_VALUE
            while (right <= str1.lastIndex) {
                if (--map[str1[right].code] >= 0) {
                    // 真正多提供了一个字符
                    count--
                }
                if (count == 0) {                    //



                    // ，移动left缩小滑动窗口

                    while (map[str1[left].code] < 0) {
                        // 当前字符过剩，可以继续往左移
                        map[str1[left++].code]++
                    }
                    // 已经无法缩小了，求最小长度
                    minLength = min(minLength, right - left + 1)
                    // 继续往左移
                    count++
                    map[str1[left++].code]++
                }
                right++ // 不满足所有字符，移动right扩大滑动窗口
            }
            return if (minLength == Int.MAX_VALUE) 0 else minLength
        }
    }
}