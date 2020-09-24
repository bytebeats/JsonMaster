package me.bytebeats.jsonmstr.util

import java.awt.Component
import java.awt.Cursor

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/9/24 17:40
 * @Version 1.0
 * @Description TO-DO
 */

class ViewUtils {
    companion object {
        fun setCursor(cursor: Int, vararg components: Component) {
            for (c in components) {
                c.cursor = Cursor.getPredefinedCursor(cursor)
            }
        }
    }
}