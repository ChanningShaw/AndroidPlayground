package com.wedream.demo.algo.action

sealed class AlgorithmAction(open var delayTime: Long = DEFAULT_DELAY_TIME) {
    companion object {
        const val DEFAULT_DELAY_TIME = 800L
        const val MSG_DELAY_TIME = 300L
    }

    open class MessageAction(
        var msg: String,
        override var delayTime: Long = MSG_DELAY_TIME
    ) : AlgorithmAction()

    object FinishAction : AlgorithmAction()

    open class ArrayAction : AlgorithmAction()

    open class LinkedListAction : AlgorithmAction()
}