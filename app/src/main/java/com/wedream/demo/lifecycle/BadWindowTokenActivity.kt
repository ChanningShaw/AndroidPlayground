package com.wedream.demo.lifecycle

import android.os.Bundle
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.wedream.demo.R

class BadWindowTokenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bad_window_token)
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        return super.onTouchEvent(event)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    fun showTemplateProcessDialog(fragmentManager: FragmentManager): DialogFragment {
        val dialog = MyDialogFragment()
        val ft: FragmentTransaction = fragmentManager.beginTransaction()
        ft.add(dialog, "aaa")
        ft.commitAllowingStateLoss()
        return dialog
    }
}