package com.wedream.demo.render.gl3

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.wedream.demo.R
import com.wedream.demo.render.gl3.ShaderUtils.compileFragmentShader
import com.wedream.demo.render.gl3.ShaderUtils.compileVertexShader
import com.wedream.demo.render.gl3.ShaderUtils.linkProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10


class VertexArrayRenderer : GLSurfaceView.Renderer {

    companion object {
        private const val VERTEX_POS_SIZE = 3
        private const val VERTEX_STRIDE = VERTEX_POS_SIZE * 4
        private const val VERTEX_POS_INDEX = 0
    }


    private var vertexBuffer: FloatBuffer? = null


    private var mProgram = 0

    /**
     * 点的坐标
     */
    private val vertexPoints = floatArrayOf(
        0.0f, 0.5f, 0.0f,
        -0.5f, -0.5f, 0.0f,
        0.5f, -0.5f, 0.0f
    )

    /**
     * 缓冲数组
     */
    private val vaoIds = IntArray(1)

    private val vboIds = IntArray(1)

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
        GLES30.glClearColor(0.5f, 0.5f, 0.5f, 0.5f)
        //编译
        val vertexShaderId = compileVertexShader(ResReadUtils.readResource(R.raw.vertex_array_shader))
        val fragmentShaderId = compileFragmentShader(ResReadUtils.readResource(R.raw.fragment_array_shader))
        //鏈接程序片段
        mProgram = linkProgram(vertexShaderId, fragmentShaderId)

        //生成1个缓冲ID
        GLES30.glGenVertexArrays(1, vaoIds, 0)

        //绑定VAO
        GLES30.glBindVertexArray(vaoIds[0])

        //1. 生成1个缓冲ID
        GLES30.glGenBuffers(1, vboIds, 0)
        //2. 向顶点坐标数据缓冲送入数据，把顶点数组复制到缓冲中
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vboIds[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, vertexPoints.size * 4, vertexBuffer, GLES30.GL_STATIC_DRAW)

        //3. 将顶点位置数据送入渲染管线
        GLES30.glVertexAttribPointer(VERTEX_POS_INDEX, VERTEX_POS_SIZE, GLES30.GL_FLOAT, false, VERTEX_STRIDE, 0)
        //启用顶点位置属性
        GLES30.glEnableVertexAttribArray(VERTEX_POS_INDEX)

        //4. 解绑VAO
        GLES30.glBindVertexArray(0)
    }


    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT)
        //使用程序片段
        GLES30.glUseProgram(mProgram)

        //5. 绑定VAO
        GLES30.glBindVertexArray(vaoIds[0])

        //6. 开始绘制三角形
        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3)

        //7. 解绑VAO
        GLES30.glBindVertexArray(0)
    }
}