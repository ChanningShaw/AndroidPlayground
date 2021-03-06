package com.wedream.demo.investment

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wedream.demo.R
import com.wedream.demo.common.CommonAdapter
import com.wedream.demo.util.TimeUtils.getHourAndMinutes

class NewsListAdapter(context: Context) :
    CommonAdapter<BTCPredictActivity.News, NewsListAdapter.Holder>(context) {

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val time: TextView = itemView.findViewById(R.id.time)
        val text: TextView = itemView.findViewById(R.id.text)
        val bullCount: TextView = itemView.findViewById(R.id.bull_count)
        val bearCount: TextView = itemView.findViewById(R.id.bear_count)
    }

    override fun getItemLayout(): Int {
        return R.layout.item_news_list
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        super.onBindViewHolder(holder, position)
        val news = getData()[position]
        holder.time.text = getHourAndMinutes(news.time)
        holder.text.text = news.title
        holder.bullCount.text = news.bullCount.toString()
        holder.bearCount.text = news.bearCount.toString()
    }

    override fun getViewHolder(view: View): Holder {
        return Holder(view)
    }
}