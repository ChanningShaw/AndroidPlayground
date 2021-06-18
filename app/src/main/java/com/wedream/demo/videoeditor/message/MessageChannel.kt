package com.wedream.demo.videoeditor.message

import com.wedream.demo.util.LogUtils.printAndDie
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.subjects.PublishSubject

object MessageChannel {
    private val _message = PublishSubject.create<KyMessage>()
    private val message: Flowable<KyMessage> get() = _message.toFlowable(BackpressureStrategy.MISSING)

    fun sendMessage(what: Int) {
        sendMessage(KyMessage.obtain().apply {
            this.what = what
        })
    }

    fun sendMessage(args: KyMessage) {
        _message.onNext(args)
        args.recycle()
    }

    fun subscribe(block: (msg: KyMessage) -> Unit) {
        val d = message.subscribe({
            block.invoke(it)
        }, {
            it.printAndDie()
        })
    }

    fun subscribe(what: Int, block: (msg: KyMessage) -> Unit) {
        val d = message.subscribe({
            if (what == it.what) {
                block.invoke(it)
            }
        }, {
            it.printAndDie()
        })
    }
}
