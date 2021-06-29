package com.wedream.demo.videoeditor.controller

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class Controller<M : ViewModel> {

    private lateinit var model: M
    private var compositeDisposable = CompositeDisposable()

    fun bind(model: M) {
        this.model = model
        onBind()
    }

    protected open fun onBind() {

    }

    protected open fun onUnBind() {

    }

    fun getModel(): M {
        return model
    }

    fun addToAutoDisposes(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun destroy() {
        onUnBind()
        compositeDisposable.clear()
    }
}