package com.wedream.demo.investment

import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

object NewsManager {
    fun queryNews(page: Int, limit: Int): Observable<List<NewsEntity>> {
        return Observable.fromCallable {
            emptyList()
        }
    }

    fun queryLast(hour: Int): Observable<List<NewsEntity>> {
        val last12 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).apply {
            add(Calendar.HOUR, -hour)
        }
        return Observable.fromCallable {
            emptyList()
        }
    }

    fun insertNews(newsList: List<NewsEntity>) {
        val d = Observable.fromCallable {

        }.subscribeOn(Schedulers.io()).subscribe()
    }
}