package com.wedream.demo.videoeditor.controller

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

abstract class ViewController<M : ViewModel> : Controller<M>() {
    private lateinit var rootView: View

    fun bind(model: M, view: View) {
        this.rootView = view
        super.bind(model)
    }

    fun getRootView(): View {
        return rootView
    }

    fun <T : View> findViewById(id: Int): T {
        return rootView.findViewById(id)
    }

    fun getActivity() : AppCompatActivity {
        return rootView.context as AppCompatActivity
    }
}