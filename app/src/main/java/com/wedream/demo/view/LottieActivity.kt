package com.wedream.demo.view

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.wedream.demo.R

class LottieActivity : AppCompatActivity() {
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