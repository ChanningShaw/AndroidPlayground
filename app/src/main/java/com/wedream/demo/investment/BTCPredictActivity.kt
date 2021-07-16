package com.wedream.demo.investment

import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.app.BaseActivity
import com.wedream.demo.util.LogUtils.log
import com.wedream.demo.util.TimeUtils.getTimeString
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.*

class BTCPredictActivity : BaseActivity() {

    private lateinit var disposable: CompositeDisposable
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

    // 缓存，按时间降序排序
    private var cache = LinkedList<NewsEntity>()

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
        disposable = CompositeDisposable()
        loadNews()
        startSpider()
    }

    private fun loadNews() {
        disposable.add(NewsManager.queryLast(12)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                cache.clear()
                cache.addAll(it)
                showNews()
            }, {
                log { "error, cause = ${it.cause} , msg = ${it.message}" }
            })
        )
    }

    private fun startSpider() {
        disposable.add(NewsSpider.start("https://www.bishijie.com/kuaixun")
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                addToCache(it)
                showNews()
            }, {
                log { "error, cause = ${it.cause} , msg = ${it.message}" }
            })
        )
    }

    /**
     * todo 这里可以优化
     */
    private fun addToCache(newsList: List<NewsEntity>) {
        for (n in newsList) {
            var add = false
            for (i in cache.indices) {
                val cn = cache[i]
                if (n == cn) {
                    cn.bullCount = n.bullCount
                    cn.bearCount = n.bearCount
                    add = true
                    break
                } else if (n.time > cn.time) {
                    cache.add(i, n)
                    add = true
                    break
                }
            }
            if (!add) {
                cache.addLast(n)
            }
        }
    }

    private fun showNews() {
        log { "total news size = ${cache.size}" }
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
        for (news in cache) {
            if (news.time > last12.timeInMillis) {
                bullLast12 += news.bullCount
                bearLast12 += news.bearCount
            }
            if (news.time > last6.timeInMillis) {
                bullLast6 += news.bullCount
                bearLast6 += news.bearCount
            }
            if (news.time > last3.timeInMillis) {
                bullLast3 += news.bullCount
                bearLast3 += news.bearCount
            }
        }
//        log { "totalBullCount = $totalBullCount, totalBearCount = $totalBearCount" }
        bullLastText3.text = bullLast3.toString()
        bearLastText3.text = bearLast3.toString()
        ratioLastText3.text = String.format("%.2f", bullLast3 * 1.0f / (bearLast3 + bullLast3) * 100) + "%"

        bullLastText6.text = bullLast6.toString()
        bearLastText6.text = bearLast6.toString()
        ratioLastText6.text = String.format("%.2f", bullLast6 * 1.0f / (bearLast6 + bullLast6) * 100) + "%"

        bullLastText12.text = bullLast12.toString()
        bearLastText12.text = bearLast12.toString()
        ratioLastText12.text = String.format("%.2f", bullLast12 * 1.0f / (bullLast12 + bearLast12) * 100) + "%"

        lastUpdate.text = getTimeString(now)
        adapter.setData(cache)
    }

    override fun onStop() {
        super.onStop()
        disposable.dispose()
        NewsManager.insertNews(cache)
    }
}