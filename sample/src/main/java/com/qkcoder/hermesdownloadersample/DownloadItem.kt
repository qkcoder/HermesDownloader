package com.qkcoder.hermesdownloadersample

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.items.AbstractItem

/**
 * * @author  tq
 * * @email   qkcoder@aliyun.com
 * * @date    2021/4/29 11:26 下午
 * * @desc
 **/
class DownloadItem : AbstractItem<DownloadItem.ViewHolder>() {
    private var progress: Int = 0
    private var url: String? = null
    private var name: String? = null

    override val layoutRes: Int
        get() = R.layout.item_download_list
    override val type: Int
        get() = 0

    fun getUrl(): String? {
        return url
    }

    fun setProgress(progress: Int) {
        this.progress = progress
    }

    fun with(progress: Int): DownloadItem {
        this.progress = progress
        return this
    }

    fun with(url: String, name: String): DownloadItem {
        this.url = url
        this.name = name
        return this
    }

    override fun getViewHolder(v: View): ViewHolder {
        return ViewHolder(v)
    }

    class ViewHolder(view: View) : FastAdapter.ViewHolder<DownloadItem>(view) {
        private var mTitleView = view.findViewById<TextView>(R.id.title_item_download_list)
        private var mProgressBar: ProgressBar =
            view.findViewById(R.id.progressbar_item_download_list)
        var mDownloadBtn: TextView = view.findViewById(R.id.btn_item_download_list)

        init {
            view.background = createCornerBg()
            mDownloadBtn.background = createCornerBg2()
        }

        override fun bindView(item: DownloadItem, payloads: List<Any>) {

            mProgressBar.progress = item.progress
            mTitleView.text = item.name
            if (item.progress < 100) {
                mDownloadBtn.isEnabled = true
                mDownloadBtn.text = if (item.progress == 0) "下载" else "下载中"
            } else {
                mDownloadBtn.isEnabled = false
                mDownloadBtn.text = "已下载"
            }
        }

        override fun unbindView(item: DownloadItem) {
        }


        private fun createCornerBg(): GradientDrawable {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setColor(Color.parseColor("#f5f5f5"))
            gradientDrawable.cornerRadius = 20f
            return gradientDrawable
        }


        private fun createCornerBg2(): GradientDrawable {
            val gradientDrawable = GradientDrawable()
            gradientDrawable.shape = GradientDrawable.RECTANGLE
            gradientDrawable.setColor(Color.parseColor("#F63939"))
            gradientDrawable.cornerRadius = 20f
            return gradientDrawable
        }

    }
}
