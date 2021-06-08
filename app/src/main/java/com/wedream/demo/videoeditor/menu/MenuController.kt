package com.wedream.demo.videoeditor.menu

import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.videoeditor.controller.Controller

class MenuController : Controller<MenuViewModel>() {

    private val menuList = listOf(
        MenuEntity("新增素材") {
            log { "新增素材 onClick" }
        }
    )

    override fun onBind() {
        val rootView = getRootView() as ViewGroup
        for (m in menuList) {
            val button = Button(getActivity())
            button.text = m.name
            val params = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            button.setOnClickListener {
                m.clickAction.invoke()
            }
            rootView.addView(button, params)
        }
    }
}