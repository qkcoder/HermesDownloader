package io.github.qkcoder.hermesdownloader

/**
 * * @author  tq
 * * @email   qkcoder@aliyun.com
 * * @date    2021/4/29 6:10 下午
 * * @desc
 **/
interface DownloaderListener {
    fun onSuccess(fileKey: String, cachePath: String, errorCode: Int)

    fun onFailed(fileKey: String, errorCode: Int)

    fun onPause(fileKey: String)

    fun onCancel(fileKey: String)

    fun onProgress(fileKey: String, downloadLength: Long, totalLength: Long)

}