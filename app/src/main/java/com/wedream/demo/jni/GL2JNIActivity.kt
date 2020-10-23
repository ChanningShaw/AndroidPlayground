package com.wedream.demo.jni

import android.app.Activity
import android.os.Bundle


class GL2JNIActivity : Activity() {
    private var mView: GL2JNIView? = null
    override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        mView = GL2JNIView(this)
        setContentView(mView)
    }

    override fun onPause() {
        super.onPause()
        mView?.onPause()
    }

    override fun onResume() {
        super.onResume()
        mView?.onResume()
    }
}
