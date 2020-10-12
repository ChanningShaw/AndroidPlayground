package com.wedream.demo.concurrent.rxjava

import android.os.Bundle
import android.widget.Button
import com.wedream.demo.R
import com.wedream.demo.app.DisposableActivity
import com.wedream.demo.util.LogUtils.log
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.AsyncSubject
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.ReplaySubject
import org.reactivestreams.Subscription
import java.util.concurrent.TimeUnit


class RxJavaDemoActivity : DisposableActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rxjava)
        findViewById<Button>(R.id.stop_all).setOnClickListener {
            dispose()
        }
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
        findViewById<Button>(R.id.cold_observable).setOnClickListener {
            // subscriber1和subscriber2 收到的信息并不是完全同步的，有时候subscriber1快一些，有时候subscriber2快一些。
            val subscriber1 = Consumer<Long> {
                log { "subscriber1 $it" }
            }
            val subscriber2 = Consumer<Long> {
                log { "subscriber2 $it" }
            }
            val observable = Observable.create<Long> {
                addToAutoDisposable(Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                    .take(Long.MAX_VALUE)
                    .subscribe(it::onNext))
            }.observeOn(Schedulers.newThread())
            observable.subscribe(subscriber1)
            observable.subscribe(subscriber2)
        }

        findViewById<Button>(R.id.hot_observable).setOnClickListener {
            // subscriber1一定会比subscriber2快。
            val subscriber1 = Consumer<Long> {
                log { "subscriber1 $it" }
            }
            val subscriber2 = Consumer<Long> {
                log { "subscriber2 $it" }
            }
            val observable = Observable.create<Long> {
                addToAutoDisposable(
                    Observable.interval(10, TimeUnit.MILLISECONDS, Schedulers.computation())
                        .take(Long.MAX_VALUE)
                        .subscribe(it::onNext)
                )
            }.observeOn(Schedulers.newThread()).publish()
            observable.connect()
            observable.subscribe(subscriber1)
            try {
                Thread.sleep(20L)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            observable.subscribe(subscriber2)
        }

        // 会接收AsyncSubject的onComplete()之前的最后一个数据。
        // subject.onComplete()必须要调用才会开始发送数据，否则Subscriber将不接收任何数据。
        findViewById<Button>(R.id.async_subject).setOnClickListener {
            val subject = AsyncSubject.create<String>()
            subject.onNext("asyncSubject1")
            subject.onNext("asyncSubject2")
            subject.onComplete()
            subject.subscribe({
                log { it }
            }, {
                it.printStackTrace()
            }, {
                log { "complete" }
            })
            subject.onNext("asyncSubject3")
            subject.onNext("asyncSubject4")
        }

        // Observer会接收到BehaviorSubject被订阅之前的最后一个数据，再接收订阅之后发射过来的数据。
        // 如果BehaviorSubject被订阅之前没有发送任何数据，则会发送一个默认数据。
        findViewById<Button>(R.id.behavior_subject).setOnClickListener {
            // behaviorSubject1 会当做默认值发送
            val subject = BehaviorSubject.createDefault("behaviorSubject1")
            subject.onNext("behaviorSubject2")
            subject.subscribe({
                log { it }
            }, {
                it.printStackTrace()
            }, {
                log { "complete" }
            })
            subject.onNext("behaviorSubject3")
            subject.onNext("behaviorSubject4")
        }

        findViewById<Button>(R.id.replay_subject).setOnClickListener {
            val subject = ReplaySubject.createWithSize<String>(1)
            subject.onNext("replaySubject1")
            subject.onNext("replaySubject2")
            subject.subscribe({
                log { it }
            }, {
                it.printStackTrace()
            }, {
                log { "complete" }
            })
            subject.onNext("replaySubject3")
            subject.onNext("replaySubject4")
        }

        findViewById<Button>(R.id.replay_subject).setOnClickListener {
            val subject = ReplaySubject.createWithSize<String>(1)
            subject.onNext("replaySubject1")
            subject.onNext("replaySubject2")
            subject.subscribe({
                log { it }
            }, {
                it.printStackTrace()
            }, {
                log { "complete" }
            })
            subject.onNext("replaySubject3")
            subject.onNext("replaySubject4")
        }

        //Observer只接收PublishSubject被订阅之后发送的数据。
        findViewById<Button>(R.id.publish_subject).setOnClickListener {
            val subject = PublishSubject.create<String>()
            subject.onNext("publicSubject1")
            subject.onNext("publicSubject2")
            subject.subscribe({
                log { it }
            }, {
                it.printStackTrace()
            }, {
                log { "complete" }
            })
            subject.onNext("publicSubject3")
            subject.onNext("publicSubject4")
            subject.onComplete();
        }


        // flowable 缓冲区默认大小128，超过会抛出io.reactivex.exceptions.MissingBackpressureException
        findViewById<Button>(R.id.flowable_consumer).setOnClickListener {
            Flowable.create(FlowableOnSubscribe<Int> {
                for (i in 0..129) {
                    it.onNext(i)
                }
            }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe({
                    log { it }
                }, {
                    it.printStackTrace()
                })
        }

        findViewById<Button>(R.id.flowable_subscriber).setOnClickListener {
            Flowable.create(FlowableOnSubscribe<Int> {
                for (i in 0..129) {
                    it.onNext(i)
                }
            }, BackpressureStrategy.BUFFER)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(object : FlowableSubscriber<Int> {
                    override fun onSubscribe(s: Subscription) {
                        s.request(10)
                    }

                    override fun onNext(t: Int?) {
                        log { t }
                    }

                    override fun onError(t: Throwable?) {
                        t?.printStackTrace()
                    }

                    override fun onComplete() {
                        log { "onComplete" }
                    }
                })
        }
    }
}