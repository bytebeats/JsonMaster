package me.bytebeats.jsonmstr.exc

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/9/24 17:37
 * @Version 1.0
 * @Description TO-DO
 */

open class ParseException @JvmOverloads constructor(
    message: String,
    cause: Throwable,
    enableSuppression: Boolean,
    writableStackTrace: Boolean
) : RuntimeException("ParseException: $message", cause, enableSuppression, writableStackTrace)