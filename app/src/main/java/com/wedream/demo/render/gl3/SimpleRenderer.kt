package com.wedream.demo.render.gl3

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class SimpleRenderer : GLSurfaceView.Renderer {

    private val vertexPoints = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )

    private val color = floatArrayOf(
        0.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f
    )

    companion object {
        private val POSITION_COMPONENT_COUNT = 3
    }

    private var vertexBuffer: FloatBuffer? = null

    private var colorBuffer: FloatBuffer? = null

    var program: Int? = null

    /**
     * 顶点着色器
     */
    // vColor 由 定点着色器 传到 片段着色器
    private val vertextShader = """
        #version 300 es 
        layout (location = 0) in vec4 vPosition;
        layout (location = 1) in vec4 aColor;
        out vec4 vColor;
        void main() {
            gl_Position  = vPosition;
            gl_PointSize = 10.0;
            vColor = aColor;
        }
        """.trimIndent()

    /**
     * 片段着色器
     */
    private val fragmentShader = """
        #version 300 es 
        precision mediump float;
        in vec4 vColor;
        out vec4 fragColor;
        void main() { 
            fragColor = vColor; 
        }
        """.trimIndent()

    init {
        //分配内存空间,每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        //传入指定的坐标数据
        vertexBuffer?.put(vertexPoints)
        vertexBuffer?.position(0)

        colorBuffer = ByteBuffer.allocateDirect(color.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        //传入指定的数据
        colorBuffer?.put(color)
        colorBuffer?.position(0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)

        //编译
        val vertexShaderId = ShaderUtils.compileVertexShader(vertextShader)
        val fragmentShaderId = ShaderUtils.compileFragmentShader(fragmentShader)
        //链接程序片段
        program = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId)
        //在OpenGLES环境中使用程序片段
        GLES30.glUseProgram(program!!)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)

        //准备坐标数据
        GLES30.glVertexAttribPointer(0, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, vertexBuffer)
        //启用顶点的句柄
        GLES30.glEnableVertexAttribArray(0)

        //绘制三角形颜色
        GLES30.glEnableVertexAttribArray(1)
        GLES30.glVertexAttribPointer(1, 4, GLES30.GL_FLOAT, false, 0, colorBuffer)
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)

        //禁止顶点数组的句柄
        GLES30.glDisableVertexAttribArray(0)
        GLES30.glDisableVertexAttribArray(1)

//        //准备坐标数据
//        GLES30.glVertexAttribPointer(0, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, vertexBuffer)
//        //启用顶点的句柄
//        GLES30.glEnableVertexAttribArray(0)
//        //绘制三个点
//        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)
//        //禁止顶点数组的句柄
//        GLES30.glDisableVertexAttribArray(0)
//
//        //绘制直线
//        GLES30.glVertexAttribPointer(0, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, 0, vertexBuffer)
//        GLES30.glEnableVertexAttribArray(0)
//        GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, 2)
//        GLES30.glLineWidth(10f)
//        GLES30.glDisableVertexAttribArray(0)
    }
}