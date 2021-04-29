package com.qkcoder.hermesdownloader

import java.util.concurrent.RejectedExecutionHandler
import java.util.concurrent.ThreadPoolExecutor

/**
 * * @author  tq
 * * @email   qkcoder@aliyun.com
 * * @date    2021/4/29 6:28 下午
 * * @desc
 **/
class DownloaderTaskRejectedHandler : RejectedExecutionHandler {
    override fun rejectedExecution(
        task: Runnable?,
        executor: ThreadPoolExecutor?
    ) {

    }
}