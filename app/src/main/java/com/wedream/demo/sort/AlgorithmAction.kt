package com.wedream.demo.sort

sealed class AlgorithmAction {
    class MessageAction(var msg: String) : AlgorithmAction()
    class CopyAction(var from: Int, var to: Int) : AlgorithmAction()
    class SwapAction(var p1: Int, var p2: Int) : AlgorithmAction()
    object FinishAction : AlgorithmAction()
}