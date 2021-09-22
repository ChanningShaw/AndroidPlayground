package com.wedream.demo.app

import androidx.lifecycle.ViewModel
import com.wedream.demo.util.LogUtils.log
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import javax.inject.Named

@HiltViewModel
class MainViewModel @Inject constructor(
    @Named("String1") val testString1: String
) : ViewModel() {
    init {
        log { "MainViewModel: testString1 = $testString1" }
    }
}