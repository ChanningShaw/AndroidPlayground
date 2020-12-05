package com.wedream.demo.render.gl3.native

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.wedream.demo.util.LogUtils.log
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class MyGLSurfaceView(context: Context?, attrs: AttributeSet?) : GLSurfaceView(context, attrs) {
    private val mGLRender: MyGLRender
    private val mNativeRender: MyNativeRender

    constructor(context: Context?) : this(context, null) {}

    val nativeRender: MyNativeRender
        get() = mNativeRender

    class MyGLRender internal constructor(myNativeRender: MyNativeRender) : Renderer {
        private val mNativeRender: MyNativeRender = myNativeRender
        override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
            log { "onSurfaceCreated() called with: gl = [$gl], config = [$config]" }
            mNativeRender.native_OnSurfaceCreated()
        }

        override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
            log { "onSurfaceChanged() called with: gl = [$gl], width = [$width], height = [$height]" }
            mNativeRender.native_OnSurfaceChanged(width, height)
        }

        override fun onDrawFrame(gl: GL10) {
            log { "onDrawFrame() called with: gl = [$gl]" }
            mNativeRender.native_OnDrawFrame()
        }

    }

    companion object {
        private const val TAG = "MyGLSurfaceView"
        const val IMAGE_FORMAT_RGBA = 0x01
        const val IMAGE_FORMAT_NV21 = 0x02
        const val IMAGE_FORMAT_NV12 = 0x03
        const val IMAGE_FORMAT_I420 = 0x04
    }

    init {
        setEGLContextClientVersion(3)
        mNativeRender = MyNativeRender()
        mGLRender = MyGLRender(mNativeRender)
        setRenderer(mGLRender)
        renderMode = RENDERMODE_CONTINUOUSLY
    }
}