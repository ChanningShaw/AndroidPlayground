package com.wedream.demo.algo.model

abstract class AlgorithmModel(
    open val name: String = "",
    open val title: String = "",
    open val tips: String = ""
) {
    open fun getOptions(): List<Option> {
        return emptyList()
    }

    open fun getLabels(): List<String> {
        return emptyList()
    }

    open fun onOptionSelect(option: Option) {

    }

    abstract fun execute(option: Option?): ExecuteResult
}

class Option(
    val id: Int,
    val name: String
)

class ExecuteResult(val input: String, val output: String)