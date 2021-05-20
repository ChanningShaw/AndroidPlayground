package com.wedream.demo.view.colormatrix

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.view.colormatrix.FiltersAdapter.MyViewHolder

class FiltersAdapter(
    private val mInflater: LayoutInflater,
    private val filters: List<FloatArray>
) : RecyclerView.Adapter<MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(mInflater.inflate(R.layout.item_image, parent, false))
    }

    override fun getItemCount(): Int {
        return filters.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        ColorFilter.imageViewColorFilter(holder.imageView, filters[position])
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view.findViewById(R.id.img)
    }
}