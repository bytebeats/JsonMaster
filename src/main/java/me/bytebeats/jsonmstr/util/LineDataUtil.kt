package me.bytebeats.jsonmstr.util

import me.bytebeats.jsonmstr.meta.LineData

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/14 14:28
 * @Version 1.0
 * @Description StringUtil generate LineData.
 */

object LineDataUtil {

    /**
     * get line number and column number from JsonSyntaxException
     * e.g.: Caused by: EOFException/JsonSyntaxException: End of input at line 1 column 3245 path $.timestamp
     */
    fun process(error: String): LineData? {
        if (error.isNotEmpty()) {
            try {
                var line = -1
                var column = -1
                val strs = error.split(" ")
                for (i in strs.indices) {
                    if ("line" == strs[i]) {
                        line = strs[i + 1].toInt()
                    } else if ("column" == strs[i]) {
                        column = strs[i + 1].toInt()
                    }
                }
                if (line != -1 && column != -1) {
                    return LineData(line, column)
                }
            } catch (e: Exception) {
            }
        }
        return null
    }
}