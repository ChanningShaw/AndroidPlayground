package com.wedream.demo.jni

// Wrapper for native library
object GL2JNILib {
    /**
     * @param width the current view width
     * @param height the current view height
     */
    external fun init(width: Int, height: Int)
    external fun step()

    init {
        System.loadLibrary("gl2jni")
    }
}