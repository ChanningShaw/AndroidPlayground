package com.wedream.demo.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.airbnb.lottie.LottieAnimationView
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity

class LottieActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_lottie)
        val container = window.decorView as ViewGroup
        View.inflate(this, R.layout.activity_lottie, container)
        val lottieAnimationView = container.findViewById<LottieAnimationView>(R.id.lottieView)
        lottieAnimationView.imageAssetsFolder = "lottie/images/"
        lottieAnimationView.setAnimation("lottie/data.json")
        lottieAnimationView.loop(true)
        lottieAnimationView.playAnimation()
    }
}