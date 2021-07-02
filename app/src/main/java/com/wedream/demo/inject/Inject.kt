package com.wedream.demo.inject

@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FIELD)
annotation class Inject(val value: String = "")