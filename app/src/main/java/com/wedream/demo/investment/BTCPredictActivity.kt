package com.wedream.demo.investment

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.TimeUtils.getTimeString
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jsoup.Jsoup
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class BTCPredictActivity : AppCompatActivity() {

    lateinit var disposable: Disposable
    private lateinit var bullLastText3: TextView
    private lateinit var bearLastText3: TextView
    private lateinit var ratioLastText3: TextView

    private lateinit var bullLastText6: TextView
    private lateinit var bearLastText6: TextView
    private lateinit var ratioLastText6: TextView

    private lateinit var bullLastText12: TextView
    private lateinit var bearLastText12: TextView
    private lateinit var ratioLastText12: TextView

    private lateinit var lastUpdate: TextView
    private lateinit var newsRecyclerView: RecyclerView
    private val adapter = NewsListAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_btc_predict)
        bullLastText3 = findViewById(R.id.bull_count_last_3)
        bearLastText3 = findViewById(R.id.bear_count_last_3)
        ratioLastText3 = findViewById(R.id.ratio_last_3)

        bullLastText6 = findViewById(R.id.bull_count_last_6)
        bearLastText6 = findViewById(R.id.bear_count_last_6)
        ratioLastText6 = findViewById(R.id.ratio_last_6)

        bullLastText12 = findViewById(R.id.bull_count_last_12)
        bearLastText12 = findViewById(R.id.bear_count_last_12)
        ratioLastText12 = findViewById(R.id.ratio_last_12)

        lastUpdate = findViewById(R.id.last_update_time)
        newsRecyclerView = findViewById(R.id.news_list)
        newsRecyclerView.adapter = adapter
        newsRecyclerView.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    }

    override fun onResume() {
        super.onResume()
        disposable = startSpider("https://www.bishijie.com/kuaixun")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                analyseNews(it)
            }, {
                log { "error, cause = ${it.cause} , msg = ${it.message}" }
            })
    }

    private fun analyseNews(newsList: List<News>) {
        val now = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))
        val last3 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).apply {
            add(Calendar.HOUR, -3)
        }
        val last6 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).apply {
            add(Calendar.HOUR, -6)
        }
        val last12 = Calendar.getInstance(TimeZone.getTimeZone("GMT+8")).apply {
            add(Calendar.HOUR, -12)
        }
        var bullLast3 = 0
        var bearLast3 = 0
        var bullLast6 = 0
        var bearLast6 = 0
        var bullLast12 = 0
        var bearLast12 = 0
        for (news in newsList) {
            if (news.time.after(last12)) {
                bullLast12 += news.bullCount
                bearLast12 += news.bearCount
            }
            if (news.time.after(last6)) {
                bullLast6 += news.bullCount
                bearLast6 += news.bearCount
            }
            if (news.time.after(last3)) {
                bullLast3 += news.bullCount
                bearLast3 += news.bearCount
            }
        }
//        log { "totalBullCount = $totalBullCount, totalBearCount = $totalBearCount" }
        bullLastText3.text = bullLast3.toString()
        bearLastText3.text = bearLast3.toString()
        ratioLastText3.text = String.format("%.2f", bullLast3 * 1.0f / bearLast3)

        bullLastText6.text = bullLast6.toString()
        bearLastText6.text = bearLast6.toString()
        ratioLastText6.text = String.format("%.2f", bullLast6 * 1.0f / bearLast6)

        bullLastText12.text = bullLast12.toString()
        bearLastText12.text = bearLast12.toString()
        ratioLastText12.text = String.format("%.2f", bullLast12 * 1.0f / bearLast12)

        lastUpdate.text = getTimeString(now)
        adapter.setData(newsList)
    }

    private fun startSpider(url: String): Observable<List<News>> {
        return Observable.interval(0, 10, TimeUnit.SECONDS)
            .flatMap {
                return@flatMap Observable.create<List<News>> { emitter ->
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
                        val news = mutableListOf<News>()
                        val newItems = document.getElementsByClass("newscontainer")
                        var last = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))

                        for (newsItem in newItems) {
                            val items = newsItem.getElementsByTag("li")
                            for (item in items) {
                                val h3 = item.getElementsByTag("h3").first().text()
                                val index = h3.indexOfFirst { it == ' ' }
                                val time = h3.substring(0, index)
                                log { time }
                                val ms = time.split(":")
                                log { "${ms[1].toInt()}, ${ms[0].toInt()}" }
                                val date = Calendar.getInstance(TimeZone.getTimeZone("GMT+8"))
                                date.set(
                                    last.get(Calendar.YEAR),
                                    last.get(Calendar.MONTH),
                                    last.get(Calendar.DATE),
                                    ms[0].toInt(),
                                    ms[1].toInt(),
                                    0
                                )
                                log { getTimeString(date) }
                                if (date.after(last)) {
                                    date.add(Calendar.DATE, -1)
                                    last = date
                                }
                                val title = h3.substring(index)
//                            log { time }
//                            log { title }
                                val bull = item.getElementsByClass("bull").first().text().split(" ")[1]
                                val bullCount = bull.toInt()
//                            log { bullCount }
                                val bear = item.getElementsByClass("bear").first().text().split(" ")[1]
                                val bearCount = bear.toInt()
//                            log { bearCount }
                                val n = News(date, title, bullCount, bearCount)
//                            log { n }
                                news.add(n)
                            }
                        }
                        emitter.onNext(news)
                    }
                }
            }
    }


    override fun onStop() {
        super.onStop()
        disposable.dispose()
    }

    class News(val time: Calendar, val title: String, val bullCount: Int, val bearCount: Int) {
        override fun toString(): String {
            return "time = $time, title = $title, bullCount = $bullCount, bearCount = $bearCount"
        }
    }
}