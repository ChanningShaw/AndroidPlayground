package com.wedream.demo.videoeditor

import com.wedream.demo.videoeditor.project.asset.PlacedAsset

fun PlacedAsset.contains(pos : Double) : Boolean{
    return pos in start..end
}