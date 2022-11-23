package com.wedream.demo.reflection

import android.os.Bundle
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.reflection.annotations.KeyRequirements
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.project.VideoProject

class AnnotationTestActivity : BaseActivity() {

    @Inject(AccessId.VIDEO_PROJECT)
    lateinit var videoProject: VideoProject

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        inject(VideoProject())
    }

    private fun inject(vararg objects: Any) {
        val clazz = this.javaClass
        val fields = clazz.declaredFields
        val an = objects[0].javaClass.annotations.filterIsInstance<KeyRequirements>().firstOrNull()
        for (r in an?.requirements ?: emptyArray()) {
            log { r.key }
        }
        for (field in fields) {
//            if (field.isAnnotationPresent(Inject::class.java)) {
//                val inject = field.getAnnotation(Inject::class.java)
//                log { "fieldType:${field.type}" }
//                log { "inject:$inject" }
//            }
            for (obj in objects) {
                if (obj.javaClass == field.type) {
                    field.set(this, obj)
                }
            }
        }
    }
}