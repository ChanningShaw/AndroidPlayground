package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string
import java.lang.StringBuilder
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.math.min

class ShortestStringTransPath : AlgorithmModel() {

    override var name = "字符串转换的最短路径"

    override var title = "给定两个字符串start和to，再给定一个字符串列表list，list中一定包含to，list中没有重复的字符串。" +
            "所有的字符串都是小写的。规定start每次只能改变一个字符，最终的目标是彻底变成to。但是每次变成的新字符串在list中必须存在。" +
            "请返回所有最短的变换路径。"

    override var tips = "本质上是一个图问题，首先求邻接表，即strs中的字符串都可以变到strs的哪些字符串。" +
            "然后根据邻接表广度优先求start到strs中各个字符串的距离，这个距离是最短距离。最后用深度优先求最短路径。"

    override fun execute(option: Option?): ExecuteResult {
        val input = arrayListOf("cab", "acc", "cbc", "ccc", "cac", "cbb", "aab", "abb")
        val start = "abc"
        val to = "cab"
        val out = findShortestTransPaths(start, to, input)
        return ExecuteResult("${input.string()}, $start, $to", out.string())
    }

    companion object {

        fun findShortestTransPaths(
            start: String,
            to: String,
            strs: ArrayList<String>
        ): ArrayList<LinkedList<String>> {
            val temp = ArrayList(strs)
            temp.add(start)
            val nexts = generateNext(temp) // 生成邻接表
            val distances = getDistances(start, nexts) // 计算出start到strs中各个字符串的最短距离
            val steps = LinkedList<String>()
            val res = ArrayList<LinkedList<String>>()
            getShortestPaths(start, to, nexts, distances, steps, res)
            return res
        }

        private fun getShortestPaths(
            cur: String,
            to: String,
            nexts: HashMap<String, ArrayList<String>>,
            distances: HashMap<String, Int>,
            steps: LinkedList<String>,
            result: ArrayList<LinkedList<String>>
        ) {
            steps.add(cur) // 深度优先遍历，加入自己作为起始点
            if (to == cur) {
                // 到了目的地
                result.add(LinkedList(steps))
            } else {
                for (next in nexts[cur]!!) {
                    if (distances[next] == distances[cur]!! + 1) {
                        getShortestPaths(next, to, nexts, distances, steps, result)
                    }
                }
            }
            steps.pollLast() //深度优先遍历，回退到上一步
        }

        private fun getDistances(
            start: String,
            nextMap: HashMap<String, ArrayList<String>>
        ): HashMap<String, Int> {
            val queue = LinkedList<String>()
            val set = hashSetOf<String>()
            val distanceMap = hashMapOf<String, Int>()
            distanceMap[start] = 0
            queue.add(start)
            set.add(start)
            while (queue.isNotEmpty()) {
                val cur = queue.poll()!!
                for (next in nextMap[cur]!!) { // 取出cur的所有next
                    if (!set.contains(next)) { // 如果还没有遍历过
                        distanceMap[next] = (distanceMap[cur] ?: 0) + 1
                        queue.add(next)
                        set.add(next)
                    }
                }
            }
            return distanceMap
        }

        /**
         * 找到words的中每一个word都能变成words中哪一些word
         */
        private fun generateNext(words: ArrayList<String>): HashMap<String, ArrayList<String>> {
            val next = hashMapOf<String, ArrayList<String>>()
            val set = hashSetOf<String>()
            set.addAll(words)
            for (w in words) {
                next[w] = getNextOf(w, set)
            }
            return next
        }

        /**
         * 返回words中word一次变化能够变成的值
         */
        private fun getNextOf(word: String, wordsSet: Set<String>): ArrayList<String> {
            val list = arrayListOf<String>()
            val chas = word.toCharArray()
            for (i in chas.indices) {
                for (j in 'a'.code..'z'.code) {
                    if (chas[i].code != j) {
                        // 不等就换
                        val temp = chas[i]
                        chas[i] = Char(j)
                        val str = String(chas)
                        if (wordsSet.contains(str)) { // 此变化在原列表中
                            list.add(str)
                        }
                        chas[i] = temp
                    }
                }
            }
            return list
        }
    }
}