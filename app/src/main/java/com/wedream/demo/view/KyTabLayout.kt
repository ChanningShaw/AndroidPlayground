package com.wedream.demo.view

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import android.widget.LinearLayout.HORIZONTAL
import android.widget.LinearLayout.VERTICAL
import com.wedream.demo.R
import com.wedream.demo.util.MathUtils.half


class KyTabLayout(context: Context, attrs: AttributeSet?, defStyle: Int) : HorizontalScrollView(context, attrs, defStyle) {
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context) : this(context, null, 0)

    private val wrapLayout = LinearLayout(context)
    private val tabsLayout = LinearLayout(context)
    private val indicatorLayout = FrameLayout(context)
    private val indicator = View(context)
    private val tabList = mutableListOf<Tab>()
    private var tabWidth = 0
    private var indicatorWidth = DEFAULT_INDICATOR_WIDTH
    private var previousSelect = 0
    private var currentSelect = 0
    private var minHeight = -1

    private var layoutConfig = KyTabLayoutConfig()

    private var listener: OnTabClickListener? = null

    companion object {
        const val DEFAULT_INDICATOR_WIDTH = 20
    }

    private val animator: ValueAnimator = ValueAnimator.ofFloat(1f)

    init {
        wrapLayout.orientation = VERTICAL
        tabsLayout.orientation = HORIZONTAL
        val params1 = LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        params1.weight = 1f
        wrapLayout.addView(tabsLayout, params1)
        val params2 = LayoutParams(LayoutParams.MATCH_PARENT, 10)
        wrapLayout.addView(indicatorLayout, params2)
        addView(wrapLayout, FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT).apply { isFillViewport = true })

        val params3 = LayoutParams(indicatorWidth, LayoutParams.MATCH_PARENT)
        indicator.setBackgroundColor(Color.BLUE)
        indicatorLayout.addView(indicator, params3)
        indicatorLayout.post {
            layoutIndicator()
        }
    }

    fun addTab(tab: Tab) {
        val inflater = LayoutInflater.from(context)
        val itemLayout = inflater.inflate(R.layout.item_tab_layout, null)
        val itemContent = itemLayout.findViewById<FrameLayout>(R.id.tab_item_content)
        itemContent.addView(tab.getView())
        tabList.add(tab)
        val index = tabList.size - 1
        tab.getView().setOnClickListener {
            onTabClick(tab, index)
        }
        val params = LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        params.weight = 1f
        tabsLayout.addView(itemLayout, params)
    }

    fun replaceTab(tab: Tab, index: Int) {
        tabList[index] = tab
        tabsLayout.getChildAt(index).findViewById<FrameLayout>(R.id.tab_item_content)?.let {
            it.removeAllViews()
            it.addView(tab.getView())
            tab.getView().setOnClickListener {
                onTabClick(tab, index)
            }
        }
        requestLayout()
    }

    private fun onTabClick(tab: Tab, index: Int) {
        if (currentSelect == index) {
            return
        }
        previousSelect = currentSelect
        currentSelect = index
        listener?.onTabClick(tab, index)
        post { layoutIndicator() }
    }

    private fun layoutIndicator() {
        if (tabList.isEmpty()) {
            return
        }
        val params = (indicator.layoutParams as MarginLayoutParams)
        val curLeft = params.marginStart
        val child = tabsLayout.getChildAt(currentSelect)
        val newLeft = (child.left + child.right).half() - indicator.width.half()
        val gap = newLeft - curLeft
        animator.removeAllUpdateListeners()
        animator.removeAllListeners()
        animator.addUpdateListener {
            val left = (curLeft + it.animatedFraction * gap).toInt()
            params.setMargins(left, 0, 0, 0)
            indicator.requestLayout()
        }
        animator.start()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        tabWidth = width
    }

    fun newTab(text: String): TextTab {
        return TextTab(text)
    }

    fun setIndicatorConfig(config: IndicatorConfig) {
        val params = FrameLayout.LayoutParams(config.width, config.height)
        indicator.layoutParams = params
        indicator.setBackgroundResource(config.bgRes)
        indicator.invalidate()
    }

    fun setLayoutConfig(config: KyTabLayoutConfig) {
        this.layoutConfig = config
    }

    fun setTabMinWidth(value: Int) {
        minHeight = value
    }

    abstract class Tab {
        abstract fun getView(): View
    }

    inner class TextTab(private var text: String) : Tab() {
        val tabType = TabType.Text
        var textSize: Float = 20f
        val textView: TextView = TextView(this@KyTabLayout.context)

        init {
            textView.gravity = Gravity.CENTER
            textView.text = text
            textView.textSize = 30f
        }

        override fun getView(): View {
            return textView
        }
    }

    inner class ImageTab(private var resId: Int) : Tab() {
        val tabType = TabType.Image
        val imageView: ImageView = ImageView(this@KyTabLayout.context)

        init {
            imageView.setImageResource(resId)
        }

        override fun getView(): View {
            return imageView
        }
    }

    enum class TabType {
        Text, Image
    }

    fun setOnTabClickListener(listener: OnTabClickListener) {
        this.listener = listener
    }

    interface OnTabClickListener {
        fun onTabClick(tab: Tab, index: Int)
    }
}