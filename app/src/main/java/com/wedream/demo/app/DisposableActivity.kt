package com.wedream.demo.app

import androidx.appcompat.app.AppCompatActivity
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class DisposableActivity : AppCompatActivity() {
    private var compositeDisposable = CompositeDisposable()

    fun addToAutoDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onStop() {
        super.onStop()
        dispose()
    }

    fun dispose() {
        compositeDisposable.dispose()
    }
}