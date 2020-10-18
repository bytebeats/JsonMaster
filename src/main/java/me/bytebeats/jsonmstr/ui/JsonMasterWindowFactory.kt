package me.bytebeats.jsonmstr.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/15 21:35
 * @Version 1.0
 * @Description JsonMasterWindow is the json master window displayed on right side of Intellij IDEA and is the entry of Json Master Plugin
 */

class JsonMasterWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val component = JsonMasterWindow.newInstance(project)
        component.addContentTo(toolWindow)
    }
}