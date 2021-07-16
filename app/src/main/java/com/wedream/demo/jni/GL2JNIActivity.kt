package com.wedream.demo.jni

import android.os.Bundle
import com.wedream.demo.app.BaseActivity


class GL2JNIActivity : BaseActivity() {
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
