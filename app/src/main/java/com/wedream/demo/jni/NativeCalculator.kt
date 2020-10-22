package com.wedream.demo.jni

object NativeCalculator {
    // Used to load the 'native-lib' library on application startup.
    init {
        System.loadLibrary("cal-lib")
    }

    external fun add(a: Int, b: Int): Int
}