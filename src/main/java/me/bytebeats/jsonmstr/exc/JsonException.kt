package me.bytebeats.jsonmstr.exc

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/9/24 17:38
 * @Version 1.0
 * @Description TO-DO
 */

class JsonException @JvmOverloads constructor(
    message: String,
    cause: Throwable,
    enableSuppression: Boolean,
    writableStackTrace: Boolean
) : ParseException(message, cause, enableSuppression, writableStackTrace)