package me.bytebeats.jsonmaster.intf

import javax.swing.JComponent

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/9/24 17:39
 * @Version 1.0
 * @Description ComponentProvider provides a component
 */

interface ComponentProvider {
    fun provide(): JComponent
}