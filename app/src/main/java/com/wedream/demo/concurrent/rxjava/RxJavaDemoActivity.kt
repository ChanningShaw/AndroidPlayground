package com.wedream.demo.concurrent.rxjava

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class RxJavaDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava)
        findViewById<Button>(R.id.bt_basic).setOnClickListener {
            // 可观察对象，即事件源，被观察者
            val observable: Observable<String> = Observable.create { emitter ->
                emitter.onNext("1111")
                emitter.onNext("2222")
                emitter.onNext("3333")
                emitter.onNext("4444")
                //emitter.onError(new Throwable("abc"));
                //emitter.onComplete();
            }

            //创建观察者
            val observer = object : Observer<String> {
                override fun onSubscribe(d: Disposable) {
                    log { "onSubscribe" }
                }

                override fun onNext(t: String) {
                    log { "onNext : $t" }
                }

                override fun onError(e: Throwable) {
                    log { "onError" }
                }

                override fun onComplete() {
                    log { "onComplete" }
                }
            }
            //线程切换
            observable
                //被订阅者在子线程中
                .subscribeOn(Schedulers.io())
                //订阅者在主线程中
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
        }
    }
}