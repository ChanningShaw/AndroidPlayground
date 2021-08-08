package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class MinCutCount : AlgorithmModel() {

    override var name = "回文最少分割数"

    override var title = "给定一个字符串str，返回把str全部切成回文子串的最小分割数。"

    override var tips = "动态规划。定义动态规划数组dp，dp[i]表示子串str[i..len-1]至少需要分割几次，" +
            "那么dp[0]就是最终结果。从右往左算dp的值。dp[i] = min{dp[j+1] + 1}, j属于[i, len- 1), " +
            "其中str[i..j]是回文串。时间复杂度O(n^2)"

    override fun execute(option: Option?): ExecuteResult {
        val str = "adabbca"
        val output = minCut(str)
        return ExecuteResult(str, output.string())
    }

    companion object {
        fun minCut(str: String): Int {
            if (str.isEmpty()) {
                return 0
            }
            val dp = IntArray(str.length + 1) { Int.MAX_VALUE}
            dp[str.length] = -1
            val isPalindrome = Array(str.length){ BooleanArray(str.length) }
            for (i in str.lastIndex downTo 0) {
                for (j in i..str.lastIndex) {
                    if (str[i] == str[j] && (j - i < 2 || isPalindrome[i + 1][j - 1])) {
                        isPalindrome[i][j] = true
                        dp[i] = min(dp[i], dp[j + 1] + 1)
                    }
                }
            }
            return dp[0]
        }
    }
}