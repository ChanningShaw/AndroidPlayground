package com.wedream.demo.investment

import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.TimeUtils
import io.reactivex.Observable
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.util.*
import java.util.concurrent.TimeUnit

object NewsSpider {
    fun start(url: String): Observable<List<NewsEntity>> {
        return Observable.interval(0, 10, TimeUnit.SECONDS)
            .flatMap {
                return@flatMap Observable.create<List<NewsEntity>> { emitter ->
                    var html = ""
                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url(url)
                        .build()
                    val response = client.newCall(request).execute()
                    html = response.body?.string() ?: ""
                    if (html == "") {
                        emitter.onNext(emptyList())
                    } else {
                        val document = Jsoup.parse(html)
                        val news = mutableListOf<NewsEntity>()
                        val newItems = document.getElementsByClass("newscontainer")
                        var last = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))

                        for (newsItem in newItems) {
                            val items = newsItem.getElementsByTag("li")
                            for (item in items) {
                                val h3 = item.getElementsByTag("h3").first().text()
                                val index = h3.indexOfFirst { it == ' ' }
                                val time = h3.substring(0, index)
//                                log { time }
                                val ms = time.split(":")
//                                log { "${ms[1].toInt()}, ${ms[0].toInt()}" }
                                val date = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))
                                date.set(
                                    last.get(Calendar.YEAR),
                                    last.get(Calendar.MONTH),
                                    last.get(Calendar.DATE),
                                    ms[0].toInt(),
                                    ms[1].toInt(),
                                    0
                                )
//                                log { TimeUtils.getTimeString(date) }
                                if (date.after(last)) {
                                    date.add(Calendar.DATE, -1)
                                    last = date
                                }
                                val title = h3.substring(index)
                                if (title.isEmpty()) {
                                    continue
                                }
//                            log { time }
//                            log { title }
                                val bull = item.getElementsByClass("bull").first().text().split(" ")[1]
                                val bullCount = bull.toInt()
//                            log { bullCount }
                                val bear = item.getElementsByClass("bear").first().text().split(" ")[1]
                                val bearCount = bear.toInt()
//                            log { bearCount }
                                val n = NewsEntity(UUID.randomUUID().toString(), date.timeInMillis, title, bullCount, bearCount)
//                            log { n }
                                news.add(n)
                            }
                        }
                        emitter.onNext(news)
                    }
                }
            }
    }
}