package com.wedream.demo.render.gl3

import android.app.Activity
import android.content.pm.ActivityInfo
import android.opengl.GLSurfaceView
import android.os.Bundle
import android.provider.Settings
import android.view.OrientationEventListener
import com.wedream.demo.util.LogUtils.log


class GLColorActivity : AbsGLSurfaceActivity() {

    private var myOrientationListener: MyOrientationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myOrientationListener = MyOrientationListener(this)
        val autoRotateOn = Settings.System.getInt(contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) == 1
        //检查系统是否开启自动旋转
        if (autoRotateOn) {
            myOrientationListener?.enable()
        }
    }

    override fun bindRenderer(): GLSurfaceView.Renderer {
        return RectangleRenderer()
    }

    override fun onDestroy() {
        super.onDestroy()
        //销毁时取消监听
        myOrientationListener?.disable()
    }

    class MyOrientationListener(var context: Activity, rate: Int) : OrientationEventListener(context) {
        constructor(context: Activity) : this(context, 60)

        override fun onOrientationChanged(orientation: Int) {
            log { "orientation: $orientation" }
            val screenOrientation: Int = context.resources.configuration.orientation
            if (orientation in 0..44 || orientation > 315) { //设置竖屏
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                    && orientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                ) {
                    log { "设置竖屏" }
                    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                }
            } else if (orientation in 226..314) { //设置横屏
                log { "设置横屏" }
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE) {
                    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
                }
            } else if (orientation in 46..134) { // 设置反向横屏
                log { "反向横屏" }
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) {
                    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
                }
            } else if (orientation in 136..224) {
                log { "反向竖屏" }
                if (screenOrientation != ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT) {
                    context.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT
                }
            }
        }
    }
}