package com.wedream.demo.videoeditor.timeline.widget

import android.content.Context
import android.util.AttributeSet
import com.wedream.demo.util.LogUtils.printAndDie
import com.wedream.demo.videoeditor.timeline.data.TimelineViewModel
import com.wedream.demo.view.MyFrameLayout
import io.reactivex.disposables.Disposable

class SegmentContainer(context: Context, attrs: AttributeSet?, defStyle: Int) :
    MyFrameLayout(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private var disposable : Disposable ?= null

    fun setViewModel(viewModel: TimelineViewModel) {
        val disposable = viewModel.message.subscribe({

        }, {
            it.printAndDie()
        })
    }
}