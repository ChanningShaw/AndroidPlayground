package com.wedream.demo

import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import com.ss.android.ugc.aweme.ecommerce.trackimpl.setPreviousTrackNode
import com.wedream.demo.app.track.TrackParams
import com.wedream.demo.algo.activity.*
import com.wedream.demo.app.ApplicationHolder
import com.wedream.demo.app.CategoryActivity
import com.wedream.demo.app.MainViewModel
import com.wedream.demo.app.monitor.ANRHandleActivity
import com.wedream.demo.app.monitor.CrashHandlerActivity
import com.wedream.demo.app.track.*
import com.wedream.demo.category.Category
import com.wedream.demo.category.ComponentCategory
import com.wedream.demo.compose.ComposeActivity
import com.wedream.demo.concurrent.JavaExecutorActivity
import com.wedream.demo.concurrent.kotlin.CoroutineActivity
import com.wedream.demo.concurrent.kotlin.FlowActivity
import com.wedream.demo.concurrent.kotlin.FlowUIActivity
import com.wedream.demo.concurrent.kotlin.FunctionProgrammingActivity
import com.wedream.demo.concurrent.rxjava.RxJavaDemoActivity
import com.wedream.demo.game.tankwar.TankWarActivity
import com.wedream.demo.investment.BTCPredictActivity
import com.wedream.demo.jni.GL2JNIActivity
import com.wedream.demo.jni.HelloJNIActivity
import com.wedream.demo.lifecycle.BadWindowTokenActivity
import com.wedream.demo.lifecycle.LifecycleHolderTestActivity
import com.wedream.demo.media.AudioRecordActivity
import com.wedream.demo.planegeometry.PlaneGeometryActivity
import com.wedream.demo.reflection.AnnotationTestActivity
import com.wedream.demo.reflection.ClassLoaderActivity
import com.wedream.demo.reflection.FiledInjectActivity
import com.wedream.demo.reflection.dynamicproxy.DynamicProxyActivity
import com.wedream.demo.render.*
import com.wedream.demo.render.gl3.GLColorActivity
import com.wedream.demo.touch.EventDispatchActivity
import com.wedream.demo.touch.InterceptEventActivity
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.VideoEditorActivity
import com.wedream.demo.view.*
import com.wedream.demo.view.canvas.DrawViewActivity
import com.wedream.demo.view.colormatrix.ColorMatrixCategoryActivity
import com.wedream.demo.view.flowlayout.FlowLayoutActivity
import com.wedream.demo.view.hesuan.HesuanActivity
import com.wedream.demo.view.layout.DetachViewActivity
import com.wedream.demo.view.layout.OutlineActivity
import com.wedream.demo.view.layout.TranslateActivity
import com.wedream.demo.view.multitrack.TrackActivity
import com.wedream.demo.view.newtips.NewTipsActivity
import com.wedream.demo.view.newtrack.NewMultiTrackActivity
import com.wedream.demo.view.trackmove.CrossTrackMovementActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named

@AndroidEntryPoint
class MainActivity : CategoryActivity() {

    @Inject
    @Named("String2")
    lateinit var testString: String

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val lastCom = ApplicationHolder.instance.getLastResumeActivity()
        if (lastCom != componentName) {
            val intent = Intent()
            intent.component = lastCom
            try {
                intent.setPreviousTrackNode(this)
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                log { "activity not found, has renamed an activity?" }
            }
        }
        log { "testString = $testString" }
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
                ViewStubActivity::class.java,
                ScaleActivity::class.java,
                CrossTrackMovementActivity::class.java,
                NewMultiTrackActivity::class.java,
                LottieActivity::class.java,
                ColorMatrixCategoryActivity::class.java,
                DrawViewActivity::class.java,
                TranslateActivity::class.java,
                RefreshRateActivity::class.java,
                OutlineActivity::class.java,
                CanvasBitmapActivity::class.java,
                WaveView2Activity::class.java,
                SwitchActivity::class.java,
                FlowLayoutActivity::class.java,
                DetachViewActivity::class.java,
                HesuanActivity::class.java,
                SpacedLineViewActivity::class.java,
                TestTouchEventActivity::class.java,
            )
        )
        val lifecycle = Category("lifecycle").addComponentCategories(
            listOf(
                BadWindowTokenActivity::class.java,
                LifecycleHolderTestActivity::class.java,
            )
        )
        val compose = Category("compose").addComponentCategories(
            listOf(
                ComposeActivity::class.java,
            )
        )
        val game = Category("game").addComponentCategories(
            listOf(
                TankWarActivity::class.java,
            )
        )
        val multiThreading = Category("concurrence").addComponentCategories(
            listOf(
                CoroutineActivity::class.java,
                FlowActivity::class.java,
                FunctionProgrammingActivity::class.java,
                RxJavaDemoActivity::class.java,
                JavaExecutorActivity::class.java,
                FlowUIActivity::class.java,
            )
        )
        val algorithm = Category("algorithm").addComponentCategories(
            listOf(
                SortPlaygroundActivity::class.java,
                LinkedListActivity::class.java,
                LinearAlgorithmActivity::class.java,
                TreeAlgorithmActivity::class.java,
                MatrixAlgorithmActivity::class.java,
                StringAlgorithmActivity::class.java,
                SortAlgorithmActivity::class.java,
                NumberAlgorithmActivity::class.java,
                DPAlgorithmActivity::class.java,
            )
        )
        val render = Category("render").addComponentCategories(
            listOf(
                HelloJNIActivity::class.java,
                GL2JNIActivity::class.java,
                FontDemoActivity::class.java,
                GLColorActivity::class.java,
                CanvasLayerActivity::class.java,
                SubThreadWindowActivity::class.java,
            )
        )
        val editor = ComponentCategory(VideoEditorActivity::class.java)
        val recorder = ComponentCategory(AudioRecordActivity::class.java)
        val monitor = Category("monitor").addComponentCategories(
            listOf(
                CrashHandlerActivity::class.java,
                ANRHandleActivity::class.java,
            )
        )
        val touch = Category("touch").addComponentCategories(
            listOf(
                EventDispatchActivity::class.java,
                InterceptEventActivity::class.java,
            )
        )
        val inject = Category("reflection").addComponentCategories(
            listOf(
                AnnotationTestActivity::class.java,
                FiledInjectActivity::class.java,
                ClassLoaderActivity::class.java,
                DynamicProxyActivity::class.java,
            )
        )
        val investment = Category("investment").addComponentCategories(
            listOf(
                BTCPredictActivity::class.java
            )
        )
        return listOf(
            viewCategory,
            lifecycle,
            compose,
            game,
            touch,
            multiThreading,
            algorithm,
            render,
            editor,
            recorder,
            monitor,
            inject,
            investment
        )
    }

    override fun fillTrackParams(params: TrackParams) {
        params["MainActivity"] = "this is ${this.javaClass.simpleName}"
    }

    override fun onResume() {
        super.onResume()
        logEvent("page_show")
        logStart("page_stay_duration")
    }

    override fun onStop() {
        super.onStop()
        logEnd("page_stay_duration") { params, time ->
            params["duration"] = time
        }
    }
}
