package com.wedream.demo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import com.wedream.demo.algo.activity.LinearAlgorithmActivity
import com.wedream.demo.algo.activity.LinkedListActivity
import com.wedream.demo.algo.activity.SortActivity
import com.wedream.demo.algo.activity.TreeAlgorithmActivity
import com.wedream.demo.app.ApplicationHolder
import com.wedream.demo.app.CategoryActivity
import com.wedream.demo.category.Category
import com.wedream.demo.category.ComponentCategory
import com.wedream.demo.concurrent.JavaExecutorActivity
import com.wedream.demo.concurrent.kotlin.CoroutineActivity
import com.wedream.demo.concurrent.kotlin.FlowActivity
import com.wedream.demo.concurrent.kotlin.FunctionProgrammingActivity
import com.wedream.demo.concurrent.rxjava.RxJavaDemoActivity
import com.wedream.demo.inject.AnnotationTestActivity
import com.wedream.demo.investment.BTCPredictActivity
import com.wedream.demo.jni.GL2JNIActivity
import com.wedream.demo.jni.HelloJNIActivity
import com.wedream.demo.lifecycle.BadWindowTokenActivity
import com.wedream.demo.media.AudioRecordActivity
import com.wedream.demo.planegeometry.PlaneGeometryActivity
import com.wedream.demo.render.*
import com.wedream.demo.render.gl3.GLColorActivity
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.VideoEditorActivity
import com.wedream.demo.view.*
import com.wedream.demo.view.canvas.CanvasActivity
import com.wedream.demo.view.colormatrix.ColorMatrixCategoryActivity
import com.wedream.demo.view.multitrack.TrackActivity
import com.wedream.demo.view.newtips.NewTipsActivity
import com.wedream.demo.view.newtrack.NewMultiTrackActivity
import com.wedream.demo.view.trackmove.CrossTrackMovementActivity

class MainActivity : CategoryActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lastCom = ApplicationHolder.instance.getLastResumeActivity()
        if (lastCom != componentName) {
            val intent = Intent()
            intent.component = lastCom
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                log { "activity not found, has renamed an activity?" }
            }
        }
        setCategoryList(buildData())
    }

    private fun buildData(): List<Category> {
        val viewCategory = Category("view").addComponentCategories(
            listOf(
                MatrixDemoActivity::class.java,
                DrawTextDemoActivity::class.java,
                PlaneGeometryActivity::class.java,
                DrawPathActivity::class.java,
                WaveViewActivity::class.java,
                NewTipsActivity::class.java,
                TabLayoutActivity::class.java,
                ViewPagerActivity::class.java,
                TrackActivity::class.java,
                HorizontalScrollActivity::class.java,
                RecyclerViewActivity::class.java,
                ViewLevelActivity::class.java,
                ScaleViewActivity::class.java,
                ScaleActivity::class.java,
                CrossTrackMovementActivity::class.java,
                NewMultiTrackActivity::class.java,
                InterceptEventActivity::class.java,
                LottieActivity::class.java,
                ColorMatrixCategoryActivity::class.java,
                CanvasActivity::class.java,
                TranslateActivity::class.java
            )
        )
        val lifecycle = Category("lifecycle").addComponentCategories(
            listOf(
                BadWindowTokenActivity::class.java
            )
        )
        val multiThreading = Category("multiThreading").addComponentCategories(
            listOf(
                CoroutineActivity::class.java,
                FlowActivity::class.java,
                FunctionProgrammingActivity::class.java,
                RxJavaDemoActivity::class.java,
                JavaExecutorActivity::class.java
            )
        )
        val algorithm = Category("algorithm").addComponentCategories(
            listOf(
                SortActivity::class.java,
                LinkedListActivity::class.java,
                LinearAlgorithmActivity::class.java,
                TreeAlgorithmActivity::class.java
            )
        )
        val render = Category("render").addComponentCategories(
            listOf(
                HelloJNIActivity::class.java,
                GL2JNIActivity::class.java,
                FontDemoActivity::class.java,
                GLColorActivity::class.java
            )
        )
        val editor = ComponentCategory(VideoEditorActivity::class.java)
        val recorder = ComponentCategory(AudioRecordActivity::class.java)
        val inject = Category("inject").addComponentCategories(
            listOf(
                AnnotationTestActivity::class.java
            )
        )
        val investment = Category("investment").addComponentCategories(
            listOf(
                BTCPredictActivity::class.java
            )
        )
        return listOf(
            viewCategory, lifecycle, multiThreading, algorithm, render, editor, recorder, inject, investment
        )
    }
}
