package com.wedream.demo.render.gl3

import android.opengl.GLSurfaceView
import android.os.Bundle
import com.wedream.demo.app.BaseActivity


abstract class AbsGLSurfaceActivity : BaseActivity() {
    private var mGLSurfaceView: GLSurfaceView? = null

    protected abstract fun bindRenderer(): GLSurfaceView.Renderer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        mGLSurfaceView = GLSurfaceView(this)
        setContentView(mGLSurfaceView)
        //设置版本
        mGLSurfaceView?.setEGLContextClientVersion(3)
        val renderer = bindRenderer()
        mGLSurfaceView?.setRenderer(renderer)
    }
}