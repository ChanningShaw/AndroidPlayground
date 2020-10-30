package com.wedream.demo.render.gl3

import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10




class NativeColorRenderer(var color: Int) : GLSurfaceView.Renderer {
    init {
        System.loadLibrary("native-color")
    }

    external fun surfaceCreated(color: Int)

    external fun surfaceChanged(width: Int, height: Int)

    external fun onDrawFrame()

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        surfaceCreated(color)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        surfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        onDrawFrame()
    }
}