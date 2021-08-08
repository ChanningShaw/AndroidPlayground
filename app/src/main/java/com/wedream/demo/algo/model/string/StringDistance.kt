package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.min

class StringDistance : AlgorithmModel() {

    override var name = "数组中两个字符串的最小距离"

    override var title = "原问题：给定一个字符串数组strs，在给定两个字符串str1和str2，返回在strs中str1和str2的最小距离，" +
            "如果str1或str2为null或者不在strs中，返回-1\n" +
            "进阶问题：如果这是一个很频繁的查询，如果把每次查询的时间复杂度降到最低。"

    override var tips = "进阶问题：本质上是个遍历所有子数组的问题。使用hash来保存，以便快速查询。"

    override fun getOptions(): List<Option> {
        return listOf(
            Option(0, "原问题"),
            Option(1, "进阶问题")
        )
    }

    override fun execute(option: Option?): ExecuteResult {
        when (option?.id) {
            1 -> {
                val strs = arrayOf("1", "2", "3", "1", "2", "2")
                val cache = generateDistanceCache(strs)
                val input = strs.string()
                val str1 = "1"
                val str2 = "3"
                val output = cache.getDistance(str1, str2)
                return ExecuteResult("$input, $str1, $str2", output.string())
            }
            else -> {
                val strs = arrayOf("1", "2", "3", "1", "2", "2")
                val input = strs.string()
                val str1 = "1"
                val str2 = "1"
                val output = minDistance(strs, str1, str2)
                return ExecuteResult("$input, $str1, $str2", output.string())
            }
        }

    }

    companion object {
        fun minDistance(strs: Array<String>, str1: String, str2: String): Int {
            if (strs.isEmpty()) {
                return -1
            }
            var last1 = -1
            var last2 = -1
            var min = Int.MAX_VALUE
            for (i in strs.indices) {
                if (strs[i] == str1) {
                    last1 = i
                    if (last2 >= 0) {
                        min = min(min, last1 - last2)
                    }
                }
                if (strs[i] == str2) {
                    last2 = i
                    if (last1 >= 0) {
                        min = min(min, last2 - last1)
                    }
                }
            }
            return if (min == Int.MAX_VALUE) -1 else min
        }

        fun generateDistanceCache(strs: Array<String>): DistanceCache {
            return DistanceCache(strs)
        }
    }

    class DistanceCache(strs: Array<String>) {

        private val cache = hashMapOf<String, HashMap<String, Int>>()

        init {
            for (i in strs.indices) {
                val iMap = cache[strs[i]] ?: hashMapOf()
                for (j in i + 1..strs.lastIndex) {
                    if (strs[j] != strs[i]) {
                        val lastDis = iMap[strs[j]] ?: Int.MAX_VALUE
                        val newDis = min(lastDis, j - i)
                        if (newDis != lastDis) {
                            iMap[strs[j]] = newDis
                            // 同步更新j到i的距离
                            val jMap = cache[strs[j]] ?: hashMapOf()
                            jMap[strs[i]] = newDis
                            cache[strs[j]] = jMap
                        }
                    }
                }
                cache[strs[i]] = iMap
            }
        }

        public fun getDistance(str1: String, str2: String) : Int {
            if (str1 == str2) {
                return 0
            }
            return cache[str1]?.get(str2) ?: -1
        }
    }
}