package com.wedream.demo.videoeditor.controller

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel

abstract class ViewController : Controller() {

    private lateinit var rootView: View

    fun create(view: View) {
        rootView = view
    }

    fun getRootView(): View {
        return rootView
    }

    fun <T : View> findViewById(id: Int): T {
        return rootView.findViewById(id)
    }

    fun getActivity(): AppCompatActivity {
        return rootView.context as AppCompatActivity
    }
}