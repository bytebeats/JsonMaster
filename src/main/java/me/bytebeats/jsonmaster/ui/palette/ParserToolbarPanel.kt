package me.bytebeats.jsonmaster.ui.palette

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/15 20:36
 * @Version 1.0
 * @Description ParserToolbarPanel
 */

class ParserToolbarPanel(
    val properties: PropertiesComponent,
    val toolWindow: ToolWindow
) : SimpleToolWindowPanel(false, true)