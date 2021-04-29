package com.qkcoder.hermesdownloadersample

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import com.mikepenz.fastadapter.listeners.ClickEventHook
import com.mikepenz.fastadapter.listeners.addClickListener
import com.qkcoder.hermesdownloader.HermesDownloader
import com.qkcoder.hermesdownloader.HermesDownloaderListener

class MainActivity : AppCompatActivity() {
    private var mDownloadListView: RecyclerView? = null

    private val imgUrls = arrayListOf<String>(
        "https://dimg07.c-ctrip.com/images/10081f000001gqgew105A_R_1600_10000.jpg",
        "https://dimg01.c-ctrip.com/images/fd/tg/g4/M07/1D/95/CggYHlYkzWqAE00sADm_s3tjLws999_R_1600_10000.jpg",
        "https://dimg04.c-ctrip.com/images/100l0e00000076s1h3AA9_C_1600_1200.jpg",
        "https://dimg08.c-ctrip.com/images/100v070000002mi5972A3_R_1600_10000.jpg",
        "https://dimg03.c-ctrip.com/images/10040x000000l57v800F4_R_1600_10000.jpg",
        "https://dimg01.c-ctrip.com/images/0101l120008fwgkz4D7CD_R_1600_10000.jpg",
        "https://dimg08.c-ctrip.com/images/10011d000001ey6v27D69_C_1600_1200.png",
        "https://dimg03.c-ctrip.com/images/10071d000001f1xw9D9DE_C_1600_1200.png",
        "https://dimg04.c-ctrip.com/images/100c1a00000190pbcAEC9_R_1600_10000.jpg",
        "https://dimg03.c-ctrip.com/images/100u1900000180ygj6FD7_C_1600_1200.jpg"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mDownloadListView = findViewById(R.id.download_list_sample)

        mDownloadListView?.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.VERTICAL,
            false
        )
        mDownloadListView?.setHasFixedSize(true)
        mDownloadListView?.itemAnimator = null

        val itemAdapter = ItemAdapter<DownloadItem>()
        val fastAdapter = FastAdapter.with(itemAdapter)
        fastAdapter.addEventHook(object : ClickEventHook<DownloadItem>() {
            override fun onBind(viewHolder: RecyclerView.ViewHolder): View? {
                if (viewHolder is DownloadItem.ViewHolder) {
                    return viewHolder.mDownloadBtn
                }
                return super.onBind(viewHolder)
            }

            override fun onClick(
                v: View,
                position: Int,
                fastAdapter: FastAdapter<DownloadItem>,
                item: DownloadItem
            ) {
                HermesDownloader.getInstance().startDownloadTask(
                    "position$position",
                    item.getUrl() ?: "",
                    object : HermesDownloaderListener {
                        override fun onSuccess(fileKey: String, cachePath: String, errorCode: Int) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    "下载成功,fileKey:$fileKey,cachePath:$cachePath",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                        override fun onFailed(fileKey: String, errorCode: Int) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@MainActivity,
                                    "下载失败,code:$errorCode",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                        }

                        override fun onPause(fileKey: String) {
                        }

                        override fun onCancel(fileKey: String) {
                        }

                        override fun onProgress(
                            fileKey: String,
                            downloadLength: Long,
                            totalLength: Long
                        ) {
                            runOnUiThread {
                                val progress: Int =
                                    (100 * downloadLength / (totalLength * 1.0f)).toInt()
                                item.setProgress(progress)
                                fastAdapter.notifyAdapterDataSetChanged()
                            }
                        }
                    }
                )
            }
        })

        mDownloadListView?.adapter = fastAdapter

        itemAdapter.add(getDownloadItemList())
    }

    private fun getDownloadItemList(): List<DownloadItem> {
        val downloadList = mutableListOf<DownloadItem>()
        for (index in 0 until imgUrls.size) {
            downloadList.add(DownloadItem().with(0).with(imgUrls[index], "下载位置${1 + index}"))
        }
        return downloadList
    }
}