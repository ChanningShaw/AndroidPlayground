package com.wedream.demo.util

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.TranslateAnimation

object ViewAnimatorHelper {
    const val DEFAULT_TIME = 200L
    const val EDITOR_POP_DEFAULT_TIME = 250L
    const val ANIMATION_FROM_BOTTOM = 1
    const val ANIMATION_FROM_TOP = 2

    /**
     * 弹出框的动画
     *
     * @param contentView 弹出框的内容
     * @param rootView 弹出框的根布局
     * @param show 弹出还是消失
     * @param duration 动画时长
     * @param maskerView: 蒙层view
     **/
    fun popWindowAnimation(contentView: View?,
                           rootView: View?,
                           decorView: ViewGroup?,
                           show: Boolean,
                           duration: Long = EDITOR_POP_DEFAULT_TIME,
                           type: Int = ANIMATION_FROM_BOTTOM,
                           maskerView: View? = null): TranslateAnimation {
        val animation: TranslateAnimation
        if (show) {
            rootView?.visibility = View.VISIBLE
            animation = if (type == ANIMATION_FROM_BOTTOM) {
                TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    0f, Animation.RELATIVE_TO_SELF, 1f, Animation.RELATIVE_TO_SELF, 0f)
            } else {
                TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    0f, Animation.RELATIVE_TO_SELF, -1f, Animation.RELATIVE_TO_SELF, 0f)
            }
        } else {
            animation = if (type == ANIMATION_FROM_BOTTOM) {
                TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f)
            } else {
                TranslateAnimation(
                    Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                    0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, -1f)
            }

            animation.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {
                }

                override fun onAnimationEnd(animation: Animation?) {
                    decorView?.removeView(rootView)
                    decorView?.removeView(maskerView)
                }

                override fun onAnimationStart(animation: Animation?) {
                }
            })
        }
        animation.duration = duration
        contentView?.startAnimation(animation)
        return animation
    }
}