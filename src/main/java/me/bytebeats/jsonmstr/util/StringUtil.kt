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

object StringUtil {
    fun getLine(error: String): Int {
        if (error.isNotEmpty()) {
            val errs = error.split(Regex.fromLiteral("\\s"))//空格
            for (i in errs.indices) {
                if ("line".equals(errs[i], true)) {
                    if (i < errs.lastIndex && errs[i + 1].matches(Regex.fromLiteral("\\d+"))) {
                        return errs[i + 1].toInt()
                    }
                }
            }
        }
        return 0
    }

    fun getLineOffset(error: String): Int {
        if (error.isNotEmpty()) {
            val errs = error.split("\\s".toRegex())//空格
            for (i in errs.indices) {
                if ("column".equals(errs[i], true)) {
                    if (i < errs.lastIndex && errs[i + 1].matches(Regex.fromLiteral("\\d+"))) {
                        return errs[i + 1].toInt()
                    }
                }
            }
        }
        return 0
    }

    fun process(raw: String, error: String): LineData? {
        if (raw.isNotEmpty()) return LineData(getLine(error), getLineOffset(error))
        return null
    }
}