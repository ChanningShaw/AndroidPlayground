package com.wedream.demo.algo.model.classics

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.util.string
import java.lang.StringBuilder
import java.util.*

class Hanoi : AlgorithmModel() {
    override var name = "汉诺塔问题"

    override var title = "有左、中、右3个塔，现在左塔上面放着标号为1~n的n个盘，请将n个盘从左移动到右塔，" +
            "注意，标号大的盘不能放在标号小的盘上面。"

    override var tips = "分治思想"

    override fun execute(option: Option?): ExecuteResult {
        val input = 3
        val output = hanoi(input)
        return ExecuteResult(input.toString(), output)
    }

    companion object {
        fun hanoi(n: Int): String {
            val builder = StringBuilder()
            process(n, "左", "右", "中", builder)
            return builder.toString()
        }

        private fun process(i: Int, start: String, end: String, other: String, builder: StringBuilder) {
            if (i == 1) {
                builder.append("move 1 from $start to $end\n")
            } else {
                process(i - 1, start, other, end, builder)
                builder.append("move $i from $start to $end\n")
                process(i - 1, other, end, start, builder)
            }
        }
    }
}