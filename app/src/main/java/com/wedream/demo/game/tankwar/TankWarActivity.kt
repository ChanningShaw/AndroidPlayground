package com.wedream.demo.game.tankwar

import android.os.Bundle
import android.view.Gravity
import android.widget.FrameLayout
import androidx.appcompat.app.AlertDialog
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity

class TankWarActivity : BaseActivity() {

    private var battleFieldView: BattleFieldView? = null
    private var navigateView: NavigateView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        kotlin.run {
            val view = BattleFieldView(this)
            view.setBackgroundResource(R.color.color_blue)
            val params = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            params.gravity = Gravity.CENTER
            addContentView(view, params)
            battleFieldView = view
        }
        kotlin.run {
            val view = NavigateView(this)
            val params = FrameLayout.LayoutParams(350, 350)
            params.gravity = Gravity.START or Gravity.BOTTOM
            params.bottomMargin = 100
            addContentView(view, params)
            navigateView = view
            navigateView?.onNavigate {
                battleFieldView?.setDirection(it)
            }
        }
    }


    override fun onBackPressed() {
        val alertDialog: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setTitle("确定退出游戏吗？")
                setPositiveButton("确定") { _, _ ->
                    this@TankWarActivity.finish()
                }
                setNegativeButton("取消") { _, _ ->
                }
            }
            builder.create()
        }
        alertDialog.show()
    }
}