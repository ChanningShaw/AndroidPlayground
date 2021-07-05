package com.wedream.demo.videoeditor.controller

import com.wedream.demo.inject.Inject
import com.wedream.demo.util.LogUtils.log
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class Controller {

    private var compositeDisposable = CompositeDisposable()

    fun bind(objects: Array<Any>) {
        inject(objects)
        onBind()
    }

    protected open fun onBind() {

    }

    protected open fun onUnBind() {

    }

    fun addToAutoDisposes(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun destroy() {
        onUnBind()
        compositeDisposable.clear()
    }

    private fun inject(objects: Array<Any>){
        val clazz = this.javaClass
        val fields = clazz.declaredFields
        for (field in fields) {
            if (field.isAnnotationPresent(Inject::class.java)) {
                // 有Inject声明
                val inject = field.getAnnotation(Inject::class.java)
                log { "fieldType:${field.type}" }
                log { "inject:$inject" }
                for (obj in objects) {
                    if (obj.javaClass == field.type) {
                        field.set(this, obj)
                    }
                }
            }
        }
    }
}