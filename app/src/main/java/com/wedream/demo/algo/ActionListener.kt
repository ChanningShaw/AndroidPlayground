package com.wedream.demo.algo

import androidx.annotation.MainThread
import com.wedream.demo.algo.action.AlgorithmAction

interface ActionListener {
    @MainThread
    fun onAction(action: AlgorithmAction)
}