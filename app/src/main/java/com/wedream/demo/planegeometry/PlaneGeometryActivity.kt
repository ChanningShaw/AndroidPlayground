package com.wedream.demo.planegeometry

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R

class PlaneGeometryActivity : AppCompatActivity() {

    private var demoView: PlaneGeometryView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plane_geometry)
        demoView = findViewById(R.id.demo_view)
        findViewById<Button>(R.id.add_circle).setOnClickListener { demoView?.addCircle() }

        findViewById<RadioGroup>(R.id.rg_modes).setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.rb_normal -> {
                    demoView?.setMode(PlaneGeometryView.Mode.Normal)
                }
                R.id.rb_bounce -> {
                    demoView?.setMode(PlaneGeometryView.Mode.Bounce)
                }
            }
        }

        findViewById<Button>(R.id.remove_all).setOnClickListener {
            demoView?.removeAll()
        }
    }
}