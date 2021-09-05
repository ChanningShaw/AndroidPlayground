package com.wedream.demo.algo.model.dp

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import kotlin.math.pow

class LiveProbability : AlgorithmModel() {
    override var name = "生存的概率"

    override var title = "给定地图的大小N*M，和一个人的起始位置x,y，现在这个人要走k步，每次移动都是等概率上下左右移动一步，" +
            "如果这个人移动出地图，那么他就会死，求这个人最后活下来的概率。"

    override var tips = "暴力递归转动态规划"

    override fun execute(option: Option?): ExecuteResult {
        val N = 5
        val M = 5
        val x = 2
        val y = 2
        val step = 3
        val output = recurse(N, M, x, y, step)
        val total = 4.0.pow(step)
        return ExecuteResult(
            "M = $N, M = $M, x = $x, y = $y, step = $step",
            (output / total).toString()
        )
    }

    companion object {
        /**
         * @param x 当前的横坐标
         * @param y 当前的纵坐标
         * @param step 当前剩余的步数
         *
         * 暴力递归要正推理解，动态规划是反推理解。
         */
        private fun recurse(N: Int, M: Int, x: Int, y: Int, step: Int): Int {
            if (x < 0 || x >= N || y < 0 || y >= M) { // 越界检测
                return 0
            }
            if (step == 0) {
                return 1 // 如果没有步数可走了，还没有越界，成功找到一种生存方法
            }
            return recurse(N, M, x - 1, y, step - 1) +
                    recurse(N, M, x + 1, y, step - 1) +
                    recurse(N, M, x, y - 1, step - 1) +
                    recurse(N, M, x, y + 1, step - 1)
        }
    }
}