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
 * @Description TO-DO
 */

class JsonMasterToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val component = JsonMasterComponent.newInstance(project)
        component.initJsonMaster(toolWindow)
    }
}