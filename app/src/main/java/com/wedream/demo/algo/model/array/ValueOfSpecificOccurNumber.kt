package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder

class ValueOfSpecificOccurNumber : AlgorithmModel() {

    override var name = "在数组中找到出现次数大于N/K的数"

    override var title = "给定一个整型数组arr，找出其中出现次数大鱼一半的数，如果没有打印提示信息。" +
            "进阶问题：打印所有出现次数超过N/K的数，如果没有，打印提示信息"

    override var tips = "对消原则。对于每一个数，让其和其它数对消，最后剩下来的数，就是要求的数。"

    override fun getOptions(): List<Option> {
        return listOf(
            Option(0, "出现次数大于1/2"),
            Option(1, "出现次数大于1/3")
        )
    }

    override fun execute(option: Option?): ExecuteResult {
        return when (option?.id) {
            1 -> {
                val arr = intArrayOf(1, 2, 2, 2, 3, 3, 3, 3)
                val result = getKNumber(arr, 3)
                ExecuteResult(arr.string(), result.string())
            }
            else -> {
                val arr = intArrayOf(1, 3, 2, 2, 2)
                val result = getHalfNumber(arr)
                ExecuteResult(arr.string(), result.string())
            }
        }

    }

    companion object {
        fun getHalfNumber(arr: IntArray): String {
            var candidate = 0
            var times = 0
            for (i in arr) {
                when {
                    times == 0 -> {
                        candidate = i
                        times = 1
                    }
                    candidate == i -> {
                        times++
                    }
                    else -> {
                        times--
                    }
                }
            }
            times = 0
            for (i in arr) {
                if (i == candidate) {
                    times++
                }
            }
            return if (times > arr.size / 2) {
                candidate.toString()
            } else {
                "not such number."
            }
        }

        fun getKNumber(arr: IntArray, k: Int): String {
            if (k < 2) {
                return "k is not valid."
            }
            val candidates = hashMapOf<Int, Int>()
            for (i in arr) {
                if (candidates.containsKey(i)) {
                    candidates[i] = candidates[i]!! + 1
                } else {
                    if (candidates.size == k - 1) {
                        // 已经有k-1个候选，所有候选同时消除一点
                        allCandidateMinusOne(candidates)
                    } else {
                        candidates[i] = 1
                    }
                }
            }
            val reals = getReals(arr, candidates)
            var hasValid = false
            val builder = StringBuilder()
            for (entity in reals) {
                if (entity.value > arr.size / k) {
                    hasValid = true
                    builder.append("${entity.key} :${entity.value}\n")
                }
            }
            if (!hasValid) {
                builder.append("no such number.")
            }
            return builder.toString()
        }

        private fun allCandidateMinusOne(map: HashMap<Int, Int>) {
            val listToBeDeleted = arrayListOf<Int>()
            for (entity in map) {
                if (entity.value == 1) {
                    listToBeDeleted.add(entity.key)
                }
                map[entity.key] = entity.value - 1
            }
            for (i in listToBeDeleted) {
                map.remove(i)
            }
        }

        private fun getReals(arr: IntArray, map: HashMap<Int, Int>): HashMap<Int, Int> {
            val reals = hashMapOf<Int, Int>()
            for (i in arr) {
                if (map.containsKey(i)) {
                    reals[i] = (reals[i] ?: 0) + 1
                }
            }
            return reals
        }
    }
}