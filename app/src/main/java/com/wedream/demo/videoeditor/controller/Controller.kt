package com.wedream.demo.videoeditor.controller

import com.wedream.demo.inject.Inject
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class Controller {

    private var compositeDisposable = CompositeDisposable()
    private var objectsContext = arrayListOf<Any>()

    fun bind(objects: List<Any>?) {
        objects?.let {
            objectsContext.addAll(it)
            inject(this, it)
        }
        onBind()
    }

    protected fun getObjectsContext(): List<Any> {
        return objectsContext
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

    private fun inject(target: Any, objects: List<Any>) {
        val clazz = target.javaClass
        val fields = clazz.fields
        for (field in fields) {
            if (field.isAnnotationPresent(Inject::class.java)) {
                // 有Inject声明
                val inject = field.getAnnotation(Inject::class.java)
//                log { "fieldType:${field.type}" }
//                log { "inject:$inject" }
                for (obj in objects) {
                    if (obj.javaClass == field.type || field.type.isAssignableFrom(obj.javaClass)) {
                        field.set(target, obj)
                    }
                }
            }
        }
    }
}