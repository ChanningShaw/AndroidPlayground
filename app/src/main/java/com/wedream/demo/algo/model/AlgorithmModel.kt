package com.wedream.demo.algo.model

abstract class AlgorithmModel(
    open var name: String = "",
    open var title: String = "",
    open var tips: String = ""
) {
    open fun getOptions(): List<Option> {
        return emptyList()
    }

    abstract fun execute(option: Option?): ExecuteResult
}

class Option(
    val id: Int,
    val name: String
)

class ExecuteResult(val input: String, val output: String)