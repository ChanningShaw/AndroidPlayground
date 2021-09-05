package com.wedream.demo.algo.model.number

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option

class RandomNum : AlgorithmModel() {

    override var name = "从随机5到随机7"

    override var title = "原问题：给定一个等概率随机产生1-5整数的函数rand1To5,不在使用任何额外的随机机制，实现随机产生1-7" +
            "随机函数rand1To7\n" +
            "补充问题：给定一个以p概率产生0，以1-p概率产生1的随机函数rand01p，请用rand01p实现等概率产生1-6随机函数rand1To6"

    override var tips = "插空儿，筛"

    override fun getOptions(): List<Option> {
        return listOf(
            Option(0, "rand1To6"),
            Option(1, "rand1To7")
        )
    }

    override fun execute(option: Option?): ExecuteResult {
        when (option?.id) {
            1 -> {
                val num = rand1To7()
                return ExecuteResult("无", "$num")
            }
            else ->{
                val num = rand1To6()
                return ExecuteResult("无", "$num")
            }
        }
    }

    companion object {
        /**
         * 随机产生1-5之间的整数
         */
        fun rand1To5(): Int {
            return (Math.random() * 5).toInt() + 1
        }

        fun rand01p(): Int {
            var p = 0.83
            return if (Math.random() < p) 0 else 1
        }

        fun rand1To7(): Int {
            var num: Int
            do {
                /**
                 * rand1To5() - 1 : 0-4
                 * (rand1To5() - 1) * 5 : 0,5,10,15,20
                 * (rand1To5() - 1) * 5 + rand1To5() -1 0..24
                 */
                num = (rand1To5() - 1) * 5 + rand1To5() - 1
            } while (num > 20) // 0..20
            return num % 7 + 1 // 0..6 + 1
        }

        /**
         * 随机产生0、1
         * 原理：产生01和10等概率
         */
        fun rand01(): Int {
            var num = 0
            do {
                num = rand01p()
            } while (num == rand01p()) // 如果相等，说明是00或者11，就重做
            return num
        }

        fun rand0To3(): Int {
            return rand01() * 2 + rand01()
        }

        fun rand1To6(): Int {
            var num = 0
            do {
                num = rand0To3() * 4 + rand0To3()
            } while (num > 11) // 0..11
            return num % 6 + 1 // 0..5 + 1
        }
    }
}