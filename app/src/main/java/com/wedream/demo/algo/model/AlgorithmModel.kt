package com.wedream.demo.algo.model

abstract class AlgorithmModel(
    open var name: String = "",
    open var title: String = "",
    open var tips: String = ""
) {
    open fun getOptions(): List<Option> {
        return emptyList()
    }

    abstract fun execute(option: Option?): Pair<String, String>
}

class Option(
    val id: Int,
    val name: String
)