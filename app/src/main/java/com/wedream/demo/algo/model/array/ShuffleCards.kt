package com.wedream.demo.algo.model.array

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.ArrayUtils.swap
import com.wedream.demo.util.string
import java.util.*
import kotlin.math.nextDown
import kotlin.random.Random

class ShuffleCards : AlgorithmModel() {

    override var name = "洗牌算法"

    override var title = "实现洗牌算法"

    override var tips = "每次随机的范围是[0,k]，然后把随机到的数作为下标，把对应下标的值交换到k位置，k--"

    override fun execute(option: Option?): ExecuteResult {
        val arr = intArrayOf(1, 2, 3, 4, 5, 6)
        val input = arr.string()
        shuffleCards(arr)
        val output = arr.string()
        return ExecuteResult(input, output.string())
    }

    companion object {
        fun shuffleCards(arr: IntArray) {
            if (arr.size < 2) {
                return
            }
            for (i in arr.lastIndex downTo 1) {
                val randomIndex = Random.nextInt(i + 1)
                swap(arr, i, randomIndex)
            }
        }
    }

    private class Program(val cost: Int, val profit: Int)
}