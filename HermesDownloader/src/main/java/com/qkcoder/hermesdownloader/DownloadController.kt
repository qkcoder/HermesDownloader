package com.qkcoder.hermesdownloader

/**
 * * @author  tq
 * * @email   qkcoder@aliyun.com
 * * @date    2021/4/29 10:06 下午
 * * @desc
 **/
interface DownloadController {

    fun isPaused(): Boolean

    fun isCanceled(): Boolean

}