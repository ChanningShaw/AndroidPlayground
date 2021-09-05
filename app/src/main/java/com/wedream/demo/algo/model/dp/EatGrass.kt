package com.wedream.demo.algo.model.dp

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import kotlin.math.pow

class EatGrass : AlgorithmModel() {
    override var name = "牛吃草问题"

    override var title = "现在有n份草，A和B两头牛分别轮流去吃草，但是每次只能吃4^n(n=0，1，2,3)份草，谁先让对方无草可吃就赢了。" +
            "请问a和b谁赢了。"

    override var tips = "打表法"

    override fun execute(option: Option?): ExecuteResult {
        val n = 5
        val output = if (eatGrass2(n)) "A" else "B"
        return ExecuteResult(n.toString(), output)
    }

    companion object {
        /**
         * 返回true，先手赢，否则是后手赢
         */
        fun eatGrass1(n: Int): Boolean {
            if (n < 5) {
                return n != 0 && n != 2
            }
            var base = 1
            while (base <= n) { // 逐个尝试 4^n 种先手吃法能不能赢
                if (!eatGrass1(n - base)) { // 子过程后手赢了，也就是我赢了
                    return true
                }
                if (base > n / 4) { // 防止溢出
                    break
                }
                base *= 4
            }
            return false
        }

        fun eatGrass2(n: Int): Boolean {
            return n % 5 != 0 && n % 5 != 2
        }
    }
}