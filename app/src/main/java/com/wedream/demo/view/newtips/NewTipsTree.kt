package com.wedream.demo.view.newtips

import android.view.View
import com.wedream.demo.util.LogUtils.log

object NewTipsTree {

    private val treeRoot = Node()
    private val map = hashMapOf<String, Node>()

    class Node(val view: NewTipsView? = null) {
        val children = mutableListOf<Node>()

        fun addChild(node: Node) {
            children.add(node)
            map[node.getKey()] = node
        }

        private fun getKey(): String {
            return view?.spKey ?: "root"
        }

        override fun toString(): String {
            return "NewTisNode ${getKey()} "
        }
    }

    private fun addToRoot(view: NewTipsView) {
        val node = Node(view)
        treeRoot.addChild(node)
    }

    fun addNode(parentKey: String?, view: NewTipsView) {
        if (parentKey.isNullOrEmpty()) {
            addToRoot(view)
        } else {
            map[parentKey]?.addChild(Node(view))
        }
        update()
    }

    fun update() {
        updateInternal(treeRoot)
    }

    private fun updateInternal(root: Node): Boolean {
        var childResult = false
        for (node in root.children) {
            childResult = childResult or updateInternal(node)
        }
        val result = if (root.children.size > 0) {
            childResult
        } else {
            root.view?.visible() ?: false
        }
        root.view?.visibility = if (result) View.VISIBLE else View.GONE
        log{ "result = $result, node = ${root} , visibility = ${root.view?.visibility}"}
        return result
    }
}