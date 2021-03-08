package com.wedream.demo.investment

import com.wedream.demo.app.ApplicationHolder
import com.wedream.demo.database.greenDao.NewsEntityDao
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import java.util.*

object NewsManager {
    fun queryNews(page: Int, limit: Int): Observable<List<NewsEntity>> {
        return Observable.fromCallable {
            ApplicationHolder.instance.getDaoSession().newsEntityDao.queryBuilder()
                .limit(limit)
                .offset(page)
                .list()
        }
    }

    fun queryLast(hour: Int): Observable<List<NewsEntity>> {
        val last12 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).apply {
            add(Calendar.HOUR, -hour)
        }
        return Observable.fromCallable {
            ApplicationHolder.instance.getDaoSession().newsEntityDao.queryBuilder()
                .where(NewsEntityDao.Properties.Time.gt(last12.timeInMillis))
                .list()
        }
    }

    fun insertNews(newsList: List<NewsEntity>) {
        val d = Observable.fromCallable {
            ApplicationHolder.instance.getDaoSession().newsEntityDao.insertOrReplaceInTx(newsList)
        }.subscribeOn(Schedulers.io()).subscribe()
    }
}