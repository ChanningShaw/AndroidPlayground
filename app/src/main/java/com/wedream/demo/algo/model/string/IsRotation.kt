package com.wedream.demo.algo.model.string

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.model.classics.KMP
import com.wedream.demo.util.string

class IsRotation : AlgorithmModel() {

    override var name = "判断两个字符串是否互为旋转词"

    override var title = "如果一个字符串为str，那字符串的前面任意部分挪到后面形成的字符串叫做str的旋转词。" +
            "例如，str=\"12345\"，str的旋转词有\"34512\",\"23451\"等等。给定字符串str1和str2，判断他们是否互为旋转词。"

    override var tips = "将两个str2合并成新的字符串str，判断str判断str是否包含str1即可。判断子串可以用KMP算法。"

    override fun execute(option: Option?): ExecuteResult {
        val str1 = "12345"
        val str2 = "45123"
        val output = isRotation(str1, str2)
        return ExecuteResult("$str1, $str2", output.string())
    }

    companion object {
        fun isRotation(str1: String, str2: String): Boolean {
            if (str1.length != str2.length) {
                return false
            }
            val str = str2 + str2
            return KMP.getIndexOf(str, str1) != -1
        }
    }
}