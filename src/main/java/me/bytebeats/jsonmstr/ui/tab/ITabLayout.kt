package me.bytebeats.jsonmstr.ui.tab

import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.impl.JBEditorTabs
import javax.swing.JComponent

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/13 20:18
 * @Version 1.0
 * @Description TO-DO
 */

interface ITabLayout {
    fun addTab(component: JComponent, title: String): ITabLayout
    fun getTabCount(): Int
    fun getTabAt(position: Int): TabInfo
    fun closeTab(position: Int): ITabLayout
    fun closeCurrentTab(): ITabLayout
    fun getComponent(): JBEditorTabs
    fun getTitleAt(position: Int): String
    fun getCurrentTabComponent(): JComponent
}