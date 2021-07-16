package com.wedream.demo.planegeometry

import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.Vector2D

class PlaneGeometryActivity : BaseActivity() {

    private var demoView: PlaneGeometryView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plane_geometry)
        demoView = findViewById(R.id.demo_view)
        findViewById<Button>(R.id.add_circle).setOnClickListener { demoView?.addCircle() }
        findViewById<Button>(R.id.add_line).setOnClickListener {
            Vector2D(1f, 0f).angleWith(Vector2D(-1f, 1f))
        }


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

    override fun onStop() {
        super.onStop()
        demoView?.removeAll()
    }
}