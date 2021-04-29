package com.qkcoder.hermesdownloader

import java.util.concurrent.TimeUnit

/**
 * * @author  tq
 * * @email   qkcoder@aliyun.com
 * * @date    2021/4/29 6:21 下午
 * * @desc
 **/
class DownloaderThreadPoolConfig {
    private var corePoolSize: Int = 64
    private var threadCount: Int = 128
    private var keepAliveTime: Long = 10
    private var timeUnit: TimeUnit = TimeUnit.SECONDS

    fun getCorePoolSize(): Int {
        return corePoolSize
    }

    fun getThreadCount(): Int {
        return threadCount
    }

    fun getKeepAliveTime(): Long {
        return keepAliveTime
    }


    fun getTimeUnit(): TimeUnit {
        return timeUnit;
    }
}