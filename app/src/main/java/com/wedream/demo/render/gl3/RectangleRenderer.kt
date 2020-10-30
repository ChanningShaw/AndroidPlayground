package com.wedream.demo.render.gl3

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class RectangleRenderer : GLSurfaceView.Renderer {

    private val vertextShader = """
        #version 300 es
        layout (location = 0) in vec4 vPosition;
        layout (location = 1) in vec4 aColor;
        uniform mat4 u_Matrix;
        out vec4 vColor;
        void main() {
             gl_Position  =  u_Matrix * vPosition;
             gl_PointSize = 10.0;
             vColor = aColor;
        }
        """.trimIndent()


    private val fragmentShader = """
       #version 300 es
       precision mediump float;
       in vec4 vColor;
       out vec4 fragColor;
       void main() {
            fragColor = vColor;
       }
        """.trimIndent()

    private val vertexPoints = floatArrayOf( //前两个是坐标,后三个是颜色RGB
        0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
        -0.5f, -0.5f, 1.0f, 1.0f, 1.0f,
        0.5f, -0.5f, 1.0f, 1.0f, 1.0f,
        0.5f, 0.5f, 1.0f, 1.0f, 1.0f,
        -0.5f, 0.5f, 1.0f, 1.0f, 1.0f,
        -0.5f, -0.5f, 1.0f, 1.0f, 1.0f,
        0.0f, 0.25f, 0.5f, 0.5f, 0.5f,
        0.0f, -0.25f, 0.5f, 0.5f, 0.5f)

    private var vertexBuffer: FloatBuffer? = null

    private var colorBuffer: FloatBuffer? = null

    private val mMatrix = FloatArray(16)

    var program: Int = 0
    private var uMatrixLocation: Int = 0

    companion object {
        private const val POSITION_COMPONENT_COUNT = 2
        private const val COLOR_COMPONENT_COUNT = 3

        private const val BYTES_PER_FLOAT = 4

        private const val STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * BYTES_PER_FLOAT
    }

    init {
        //分配内存空间,每个浮点型占4字节空间
        vertexBuffer = ByteBuffer.allocateDirect(vertexPoints.size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
        //传入指定的坐标数据
        vertexBuffer?.put(vertexPoints)
        vertexBuffer?.position(0)
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        //设置背景颜色
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f);

        //编译
        val vertexShaderId = ShaderUtils.compileVertexShader(vertextShader)
        val fragmentShaderId = ShaderUtils.compileFragmentShader(fragmentShader)
        //鏈接程序片段
        program = ShaderUtils.linkProgram(vertexShaderId, fragmentShaderId);
        //在OpenGLES环境中使用程序片段
        GLES30.glUseProgram(program)

        val aPositionLocation = GLES30.glGetAttribLocation(program, "vPosition")
        val aColorLocation = GLES30.glGetAttribLocation(program, "aColor")
        uMatrixLocation = GLES30.glGetUniformLocation(program, "u_Matrix")

        vertexBuffer?.position(0)
        //获取顶点数组 (POSITION_COMPONENT_COUNT = 2)
        GLES30.glVertexAttribPointer(aPositionLocation, POSITION_COMPONENT_COUNT, GLES30.GL_FLOAT, false, STRIDE, vertexBuffer)

        GLES30.glEnableVertexAttribArray(aPositionLocation)

        vertexBuffer?.position(POSITION_COMPONENT_COUNT)
        //颜色属性分量的数量 COLOR_COMPONENT_COUNT = 3
        GLES30.glVertexAttribPointer(aColorLocation, COLOR_COMPONENT_COUNT, GLES30.GL_FLOAT, false, STRIDE, vertexBuffer)

        GLES30.glEnableVertexAttribArray(aColorLocation)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val aspectRatio = if (width > height) width.toFloat() / height.toFloat() else height.toFloat() / width.toFloat()
        if (width > height) {
            //横屏
            Matrix.orthoM(mMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            //竖屏
            Matrix.orthoM(mMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        GLES30.glUniformMatrix4fv(uMatrixLocation, 1, false, mMatrix, 0)
        //用前面6个点绘制矩形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLE_STRIP, 0, 6)
        //绘制6和7两个点
        GLES30.glDrawArrays(GLES30.GL_POINTS, 6, 2)
    }
}