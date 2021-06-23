package com.wedream.demo.util

import android.widget.Toast
import com.wedream.demo.app.ApplicationHolder

object ToastUtils {
    fun showToast(msg: String) {
        Toast.makeText(ApplicationHolder.instance, msg, Toast.LENGTH_SHORT).show()
    }
}