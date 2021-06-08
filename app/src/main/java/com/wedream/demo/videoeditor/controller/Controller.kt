package com.wedream.demo.videoeditor.controller

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

abstract class Controller<M : ViewModel> {

    private lateinit var model: M
    private lateinit var rootView: View
    private var compositeDisposable = CompositeDisposable()

    fun bind(model: M, view: View) {
        this.model = model
        this.rootView = view
        onBind()
    }

    protected open fun onBind() {

    }

    protected open fun onUnBind() {

    }

    fun getModel(): M {
        return model
    }

    fun getRootView(): View {
        return rootView
    }

    fun <T : View> findViewById(id: Int): T {
        return rootView.findViewById(id)
    }

    fun addToAutoDisposes(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    fun getActivity() : AppCompatActivity{
        return rootView.context as AppCompatActivity
    }

    fun destroy() {
        onUnBind()
        compositeDisposable.clear()
    }
}