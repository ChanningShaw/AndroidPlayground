package com.wedream.demo.algo.model.list

import com.wedream.demo.algo.model.AlgorithmModel
import com.wedream.demo.algo.model.ExecuteResult
import com.wedream.demo.algo.model.Option
import com.wedream.demo.algo.structure.LinkedList
import com.wedream.demo.util.string

class JosephusProblem : AlgorithmModel() {

    override var name = "环形单链表的约瑟夫问题"

    override var title = "N个人围成一圈，第一个人从1开始报数，报M的将被杀掉，下一个人接着从1开始报。\n " +
            "如此反复，最后剩下一个，求最后的胜利者。\n " +
            "例如只有三个人，把他们叫做A、B、C，他们围成一圈，从A开始报数，假设报2的人被杀掉:\n" +
            "首先A开始报数，他报1。侥幸逃过一劫。\n" +
            "然后轮到B报数，他报2。非常惨，他被杀了\n" +
            "C接着从1开始报数\n" +
            "接着轮到A报数，他报2。也被杀死了。\n" +
            "最终胜利者是C\n" +
            "输入：一个环形单链表的头结点和报数的值m\n" +
            "输出：最后生成下来的节点，且这个节点自己组成环形单向链表，其他节点均被删除\n"

    override var tips = "普通解法O(nm)：\n" +
            "两个指针遍历last和current，last指到最后一个节点，current指向头结点\n" +
            "然后两个指针同时向前移动m步，然后删除current节点。\n" +
            "重复上述过程，直到last等于current\n" +
            "进阶解法O(n)：\n" +
            "从最后一轮往回推，寻找下标（报数=下标+1）的变化规律\n" +
            "每一轮报数相当于将往前移了m，即每个位置的报数在下一轮中减少了m\n" +
            "所以往回推的时候需要加上m\n" +
            "可以得到 f(n,m) = (f(n-1, m) + m) % n"

    override fun execute(option: Option?): ExecuteResult {
        val head = LinkedList.Node(0)
        val n1 = LinkedList.Node(1)
        val n2 = LinkedList.Node(2)
        val n3 = LinkedList.Node(3).apply {
            isLast = true
        }
        head.next = n1
        n1.next = n2
        n2.next = n3
        n3.next = head
        val m = 3
        val input = head.string() + ", $m"
        val output = josephusKill2(head, m).string()
        return ExecuteResult(input, output)
    }

    companion object {
        fun josephusKill1(head: LinkedList.Node<Int>, m: Int): LinkedList.Node<Int> {
            if (head.next == null || m < 1) {
                return head
            }
            var current = head
            var last = head
            while (last.next !== head) {
                last = last.next!!
            }
            var count = 0
            while (current !== last) {
                if (++count == m) {
                    last.next = current.next
                    count = 0
                } else {
                    last = last.next!!
                }
                current = last.next!!
            }
            current.isLast = true
            return current
        }

        fun josephusKill2(head: LinkedList.Node<Int>, m: Int): LinkedList.Node<Int> {
            if (head.next == null || m < 1) {
                return head
            }
            var size = 1
            var cur = head.next
            while (cur !== head) {
                size++
                cur = cur!!.next
            }
            cur = head
            var p = getLive2(size, m)
            while (--p != 0) {
                cur = cur!!.next
            }
            cur?.next = head
            cur?.isLast = true
            return cur!!
        }

        // 返回的是第n轮应该报的数
        private fun getLive1(n: Int, m: Int): Int {
            if (n == 1) {
                return 1
            }
            // -1 和 +1 都是为了进行坐标到报数的转换
            return (getLive1(n - 1, m) - 1 + m) % n + 1
        }

        private fun getLive2(n: Int, m: Int): Int {
            var p = 0
            for (i in 2..n) {
                p = (p + m) % i
            }
            return p + 1
        }
    }
}