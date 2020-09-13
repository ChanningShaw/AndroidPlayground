package com.wedream.demo.algo.action

sealed class AlgorithmAction {
    open class MessageAction(var msg: String) : AlgorithmAction()
    object FinishAction : AlgorithmAction()

    open class ArrayAction : AlgorithmAction()

    open class LinkedListAction : AlgorithmAction()
}