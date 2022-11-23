package com.wedream.demo.reflection.annotations

@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class KeyRequirement(val key: String, val require: Boolean = true)

@Target(AnnotationTarget.CLASS)
@MustBeDocumented
annotation class KeyRequirements(vararg val requirements: KeyRequirement)