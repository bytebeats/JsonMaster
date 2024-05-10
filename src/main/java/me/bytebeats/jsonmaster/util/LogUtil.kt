package me.bytebeats.jsonmaster.util

/**
 * @author bytebeats
 * @email <happychinapc@gmail.com>
 * @github https://github.com/bytebeats
 * @created on 2020/9/24 17:32
 * @version 1.0
 * @description LogUtil show logs in Console
 */

object LogUtil {

    fun i(msg: String) {
        println(msg)
    }

    fun e(msg: String) {
        System.err.println(msg)
        notifyError(msg)
    }

    fun w(msg: String) {
        System.err.println(msg)
        notifyWarning(msg)
    }

    fun e(throwable: Throwable) {
        throwable.printStackTrace()
        notifyError(throwable.message ?: "")
    }
}