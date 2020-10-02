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
import io.reactivex.functions.BiFunction
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
        findViewById<Button>(R.id.bt_chain).setOnClickListener {
            Observable
                .fromCallable { "ddddd" }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { log { it } }
        }
        findViewById<Button>(R.id.bt_filter).setOnClickListener {
            Observable.create<Int> {
                it.onNext(1)
                it.onNext(2)
                it.onNext(3)
                it.onNext(4)
                it.onNext(5)
            }.filter {
                it % 2 == 0
            }.subscribe {
                log { it }
            }
        }
        findViewById<Button>(R.id.bt_map).setOnClickListener {
            Observable.create<Int> {
                it.onNext(1)
                it.onNext(2)
                it.onNext(3)
                it.onNext(4)
                it.onNext(5)
            }.map {
                it.toFloat()
            }.subscribe {
                log { it }
            }
        }
        findViewById<Button>(R.id.bt_flat_map).setOnClickListener {
            Observable.create<Int> {
                it.onNext(1)
                it.onNext(2)
                it.onNext(3)
                it.onNext(4)
                it.onNext(5)
            }.flatMap {
                Observable.fromCallable {
                    it.toFloat()
                }
            }.subscribe {
                log { it }
            }
        }
        findViewById<Button>(R.id.bt_zip).setOnClickListener {
            val o1 = Observable.create<Int> {
                it.onNext(1)
                it.onNext(2)
                it.onNext(3)
                it.onNext(4)
                it.onNext(5)
            }
            val o2 = Observable.create<Int> {
                it.onNext(6)
                it.onNext(7)
                it.onNext(8)
                it.onNext(9)
                it.onNext(10)
            }
            Observable.zip(o1, o2, BiFunction<Int, Int, String> { t1, t2 ->
                "$t1,$t2"
            }).subscribe {
                log { it }
            }
        }
    }
}