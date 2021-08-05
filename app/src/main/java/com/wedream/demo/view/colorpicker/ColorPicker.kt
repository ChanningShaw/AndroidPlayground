package com.wedream.demo.view.colorpicker

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R

class ColorPicker (context: Context, attrs: AttributeSet?, defStyle: Int) :
    RecyclerView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)
    val colors = resources?.getIntArray(R.array.prebuilt_colors)?.toList()
}