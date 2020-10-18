package me.bytebeats.jsonmstr.log

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/9/24 17:32
 * @Version 1.0
 * @Description Logger show logs in Console
 */

class Logger {
    companion object {
        fun i(msg: String) {
            println(msg)
        }

        fun d(msg: String) {
            println(msg)
        }

        fun e(msg: String) {
            System.err.println(msg)
        }

        fun e(throwable: Throwable) {
            throwable.printStackTrace()
        }
    }
}