package com.wedream.demo.algo.model

abstract class AlgorithmModel(
    open var name: String = "",
    open var title: String = "",
    open var tips: String = ""
) {
    abstract fun execute(): Pair<String, String>
}