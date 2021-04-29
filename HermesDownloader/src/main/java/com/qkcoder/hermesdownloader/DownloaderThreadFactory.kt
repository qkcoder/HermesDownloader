package com.qkcoder.hermesdownloader

import java.util.concurrent.ThreadFactory

/**
 * * @author  tq
 * * @email   qkcoder@aliyun.com
 * * @date    2021/4/29 6:27 下午
 * * @desc
 **/
class DownloaderThreadFactory : ThreadFactory {

    override fun newThread(task: Runnable?): Thread {
        return Thread(task)
    }
}