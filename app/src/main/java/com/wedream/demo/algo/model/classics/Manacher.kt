package com.wedream.demo.algo.model.classics

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import kotlin.math.max
import kotlin.math.min

class Manacher : AlgorithmModel() {
    override var name = "最长回文子串"

    override var title = "最长回文子串算法Manacher"

    override var tips = ""

    override fun execute(option: Option?): ExecuteResult {
        val s = "12321111123214"
        val output = getLongestPalindromeLength(s)
        return ExecuteResult(s, output.toString())
    }

    companion object {
        fun getLongestPalindromeLength(str: String): Int {
            val chas = getManacherChars(str)
            val pArr = IntArray(chas.size) // 回文半径数组，即每一个位置的回文半径大小，最小为1
            var R = -1 // 回文半径有边界的下一个位置，最右的有效区域是R-1位置
            var C = -1 // 中心
            var max = 0
            for (i in pArr.indices) {
                pArr[i] = if (R > i) min(pArr[2 * C - i], R - i) else 1 // 求可以快进的范围
                while (i + pArr[i] < chas.size && i - pArr[i] > -1) { // 快进之后在尝试能不能逐步扩大回文范围
                    if (chas[i + pArr[i]] == chas[i - pArr[i]]) {
                        pArr[i]++
                    } else {
                        break
                    }
                    if (i + pArr[i] > R) { // 更新R和C的值
                        R = i + pArr[i]
                        C = i
                    }
                }
                max = max(max, pArr[i])
            }
            return max - 1 // 原回文长度 = ManacherChars的回文半径 - 1
        }

        private fun getManacherChars(str: String): CharArray {
            val chas = str.toCharArray()
            val manacherChas = CharArray(chas.size * 2 + 1)

            for (i in manacherChas.indices) {
                if (i and 1 == 0) {
                    manacherChas[i] = '#'
                } else {
                    manacherChas[i] = chas[i / 2]
                }
            }
            return manacherChas
        }
    }
}