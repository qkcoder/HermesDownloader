package com.qkcoder.hermesdownloader

import android.os.Environment
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL

/**
 * * @author  tq
 * * @email   qkcoder@aliyun.com
 * * @date    2021/4/29 6:40 下午
 * * @desc
 **/
class DownloaderTask(
    fileUrl: HermesDownloadFileUrl,
    listener: DownloaderListener?,
    controller: DownloadController
) : Runnable {

    private var mDownloadFileUrl: HermesDownloadFileUrl = fileUrl
    private var mDownloadListener: DownloaderListener? = listener
    private var mDownloadController: DownloadController = controller
    private var mCacheFile: File? = null


    private fun createCacheFile(
        fileKey: String,
        fileExtensionName: String
    ): File? {
        val md5FileKey = encode(fileKey)
        val fileName = "$md5FileKey.$fileExtensionName"
        var dirFile = File(
            Environment.getExternalStorageDirectory(),
            "HermesDownloader/file/"
        )
        var isFileCreatedSuccess = false
        try {
            if (!dirFile.exists()) {
                dirFile.mkdirs()
                if (!dirFile.exists()) {
                    DownloaderContext.mAppContext?.also {
                        dirFile = it.cacheDir
                        isFileCreatedSuccess = true
                    }
                }
            } else {
                isFileCreatedSuccess = true
            }
        } catch (e: Exception) {
        }
        return if (isFileCreatedSuccess) File(dirFile, fileName) else null
    }

    override fun run() {
        var downloadedFileLength = 0L
        try {
            val httpUrl = URL(mDownloadFileUrl.mFileUrl)
            val con: HttpURLConnection = httpUrl.openConnection() as HttpURLConnection
            con.connectTimeout = 5000
            con.readTimeout = 5000
            con.requestMethod = "GET"
            val mimeType = con.getHeaderField("Content-Type")
            val totalLength = getTotalLength(con)
            val fileExtensionName = getFileExtensionNameByMimeType(mimeType)
            mCacheFile = createCacheFile(mDownloadFileUrl.mFileKey, fileExtensionName ?: "")
            if (mCacheFile == null) {
                mDownloadListener?.onFailed(mDownloadFileUrl.mFileKey, -2)
                return
            }
            val bufferInputStream = BufferedInputStream(con.inputStream)
            val byteArray = ByteArray(4096)
            val fos = FileOutputStream(mCacheFile)
            var len = bufferInputStream.read(byteArray)
            while (len != -1) {
                when {
                    mDownloadController.isCanceled() -> {
                        fos.close()
                        bufferInputStream.close()
                        con.disconnect()
                        mDownloadListener?.onCancel(mDownloadFileUrl.mFileKey)
                        return
                    }
                    mDownloadController.isPaused() -> {
                        mDownloadListener?.onPause(mDownloadFileUrl.mFileKey)
                        fos.close()
                        bufferInputStream.close()
                        con.disconnect()
                        return
                    }
                    else -> {
                        fos.write(byteArray, 0, len)
                    }
                }
                downloadedFileLength += len
                mDownloadListener?.onProgress(
                    mDownloadFileUrl.mFileKey,
                    downloadedFileLength,
                    totalLength
                )
                len = bufferInputStream.read(byteArray)
            }
            fos.close()
            bufferInputStream.close()
            con.disconnect()
            mDownloadListener?.onSuccess(
                mDownloadFileUrl.mFileKey,
                mCacheFile?.absolutePath ?: "",
                0
            )
        } catch (eh: Exception) {
            mDownloadListener?.onFailed(mDownloadFileUrl.mFileKey, -1)
        }
    }
}