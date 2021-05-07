package io.github.qkcoder.hermesdownloader

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.ExecutorService
import java.util.concurrent.ThreadPoolExecutor

/**
 * * @author  tq
 * * @email   qkcoder@aliyun.com
 * * @date    2021/4/29 6:00 下午
 * * @desc
 **/
class HermesDownloader private constructor() : DownloadController {
    private var mExecutorService: ExecutorService? = null
    private var mDownloaderStatus: DownloaderStatus = DownloaderStatus.PROGRESS
    private var mDownloaderConfig: DownloaderThreadPoolConfig = DownloaderThreadPoolConfig()
    private var mDownloaderListener: HermesDownloaderListener? = null


    private var mInnerDownloaderListener: DownloaderListener = object : DownloaderListener {
        override fun onSuccess(fileKey: String, cachePath: String, errorCode: Int) {
            mDownloaderStatus = DownloaderStatus.SUCCESS
            mDownloaderListener?.onSuccess(fileKey, cachePath, errorCode)
        }

        override fun onFailed(fileKey: String, errorCode: Int) {
            mDownloaderStatus = DownloaderStatus.FAILED
            mDownloaderListener?.onFailed(fileKey, errorCode)
        }

        override fun onPause(fileKey: String) {
            mDownloaderStatus = DownloaderStatus.PAUSE
            mDownloaderListener?.onPause(fileKey)
        }

        override fun onCancel(fileKey: String) {
            mDownloaderStatus = DownloaderStatus.CANCELED
            mDownloaderListener?.onCancel(fileKey)
        }

        override fun onProgress(fileKey: String, downloadLength: Long, totalLength: Long) {
            mDownloaderStatus = DownloaderStatus.PROGRESS
            mDownloaderListener?.onProgress(
                fileKey,
                downloadLength,
                totalLength
            )
        }
    }


    override fun isPaused(): Boolean {
        return mDownloaderStatus == DownloaderStatus.PAUSE
    }

    override fun isCanceled(): Boolean {
        return mDownloaderStatus == DownloaderStatus.CANCELED
    }

    init {
        mExecutorService = ThreadPoolExecutor(
            mDownloaderConfig.getCorePoolSize(),
            mDownloaderConfig.getThreadCount(),
            mDownloaderConfig.getKeepAliveTime(),
            mDownloaderConfig.getTimeUnit(),
            ArrayBlockingQueue(mDownloaderConfig.getThreadCount()),
            DownloaderThreadFactory(),
            DownloaderTaskRejectedHandler()
        )
    }

    fun startDownloadTask(
        fileKey: String,
        fileUrl: String,
        listener: HermesDownloaderListener?
    ) {
        mDownloaderStatus = DownloaderStatus.PROGRESS
        mDownloaderListener = listener
        mExecutorService?.submit(
            DownloaderTask(
                HermesDownloadFileUrl(fileKey, fileUrl),
                mInnerDownloaderListener,
                this
            )
        )
    }

    fun pauseDownloadTask() {
        mDownloaderStatus = DownloaderStatus.PAUSE
    }

    fun cancelDownloadTask() {
        mDownloaderStatus = DownloaderStatus.CANCELED
    }


    companion object {
        @Volatile
        @JvmStatic
        private var sInstance: HermesDownloader? = null

        @JvmStatic
        fun getInstance(): HermesDownloader = sInstance ?: synchronized(this) {
            sInstance ?: HermesDownloader().also { sInstance = it }
        }
    }
}