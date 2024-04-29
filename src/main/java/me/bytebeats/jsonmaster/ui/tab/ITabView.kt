package me.bytebeats.jsonmaster.ui.tab

import javax.swing.JComponent

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/13 20:24
 * @Version 1.0
 * @Description TO-DO
 */

interface ITabView {
    fun createTabSession()
    fun closeCurrentTabSession()
    fun getTabCount(): Int
    fun getComponent(): JComponent
    fun newVerticalParserComponent(): JComponent
    fun newHorizontalParserComponent(): JComponent
}