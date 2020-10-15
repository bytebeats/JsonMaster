package me.bytebeats.jsonmstr.ui.palette

import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.openapi.wm.ToolWindow

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/15 20:36
 * @Version 1.0
 * @Description TO-DO
 */

class JsonMasterToolWindowPanel(
    val propertiesComponent: PropertiesComponent,
    val toolWindow: ToolWindow
) : SimpleToolWindowPanel(false, true)