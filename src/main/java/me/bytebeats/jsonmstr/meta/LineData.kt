package me.bytebeats.jsonmstr.meta

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/14 12:28
 * @Version 1.0
 * @Description LineData contains line number and line offset of Parser Exception of Gson
 */

data class LineData(var number: Int = 0, var offset: Int = 0)