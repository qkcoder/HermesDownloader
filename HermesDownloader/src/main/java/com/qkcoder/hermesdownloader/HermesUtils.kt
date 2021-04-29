package com.qkcoder.hermesdownloader

import android.os.Build
import android.util.Base64
import android.webkit.MimeTypeMap
import java.io.File
import java.io.FileInputStream
import java.net.URLConnection
import java.security.MessageDigest

/**
 * * @author  tq
 * * @email   qkcoder@aliyun.com
 * * @date    2021/4/29 6:55 下午
 * * @desc
 **/

private val md5EncodeStrArray =
    arrayOf<String>("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f")


fun byteArrayToHexString(b: ByteArray): String {
    val var1 = StringBuffer()
    for (var2 in b.indices) {
        var var3: Int
        if (b[var2].also { var3 = it.toInt() } < 0) {
            var3 += 256
        }
        val var4 = var3 / 16
        var3 %= 16
        var1.append(md5EncodeStrArray[var4] + md5EncodeStrArray[var3])
    }
    return var1.toString()
}

fun hexStringToByteArray(str: String): ByteArray? {
    return if (str.length % 2 != 0) {
        null
    } else {
        val var1 = ByteArray(str.length / 2)
        var var2 = 0
        while (var2 < str.length - 1) {
            val var10000 = str[var2]
            var var4 = str[var2 + 1]
            val var3 = Character.toLowerCase(var10000)
            var4 = Character.toLowerCase(var4)
            var var5: Int = if (var3 <= '9') {
                var3.toInt() - 48
            } else {
                var3.toInt() - 97 + 10
            }
            var5 = var5 shl 4
            var5 += if (var4 <= '9') {
                var4.toInt() - 48
            } else {
                var4.toInt() - 97 + 10
            }
            if (var5 > 127) {
                var5 -= 256
            }
            val var6 = var5.toByte()
            var1[var2 / 2] = var6
            var2 += 2
        }
        var1
    }
}

fun encode(origin: String?): String? {
    return origin?.let {
        try {
            byteArrayToHexString(
                MessageDigest.getInstance("MD5").digest(String().toByteArray(charset("UTF-8")))
            )
        } catch (var2: Exception) {
            null
        }
    }
}

fun encodeBase64String(base64: String?): String? {
    val base641 = Base64.decode(base64, 0)
    return try {
        byteArrayToHexString(MessageDigest.getInstance("MD5").digest(base641))
    } catch (var2: java.lang.Exception) {
        null
    }
}

fun encode(file: File?): String? {
    return file?.let {
        var fileInputStream: FileInputStream? = null
        var fileMd5Str: String? = null
        var shouldCloseStream = false
        run {
            try {
                shouldCloseStream = true
                val messageDigest = MessageDigest.getInstance("MD5")
                fileInputStream = FileInputStream(file)
                val byteArray = ByteArray(1024)
                var len: Int = fileInputStream?.read(byteArray) ?: 0
                while (len > 0) {
                    messageDigest.update(byteArray, 0, len)
                    len = fileInputStream?.read(byteArray) ?: 0
                }
                fileMd5Str = byteArrayToHexString(messageDigest.digest())
                shouldCloseStream = false
            } catch (var13: Exception) {
                shouldCloseStream = false
            } finally {
                if (shouldCloseStream) {
                    try {
                        fileInputStream?.close()
                    } catch (e: Exception) {
                    }
                }
            }
            try {
                fileInputStream?.close()
            } catch (e1: Exception) {
            }
        }

        try {
            fileInputStream?.close()
        } catch (e2: Exception) {
        }
        fileMd5Str
    }
}

fun getFileExtensionNameByMimeType(mimeType: String?): String? {
    return MimeTypeMap.getSingleton().getExtensionFromMimeType(mimeType)
}


fun getTotalLength(urlConnection: URLConnection): Long {
    var len: Long = -1
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            len = urlConnection.contentLengthLong
        } else {
            len = urlConnection.contentLength.toLong()
            if (len < 0) {
                len = getContentLengthFromHeader(urlConnection)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return len
}

private fun getContentLengthFromHeader(urlConnection: URLConnection): Long {
    val values: List<*>? = urlConnection.headerFields["Content-Length"]
    if (values != null && !values.isEmpty()) {
        val sLength = values[0] as String?
        if (sLength != null) {
            return sLength.toLong(10)
        }
    }
    return -1
}
