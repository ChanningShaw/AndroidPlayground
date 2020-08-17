package com.wedream.demo.planegeometry

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R

class PlaneGeometryActivity : AppCompatActivity() {

    private var demoView: PlaneGeometryView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plane_geometry)
        demoView = findViewById(R.id.demo_view)
    }
}