package me.bytebeats.jsonmstr.ui.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import me.bytebeats.jsonmstr.ui.JsonMasterWindow
import me.bytebeats.jsonmstr.ui.tab.ITabView
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/16 15:19
 * @Version 1.0
 * @Description TO-DO
 */

class NewWindowAction(val tabView: ITabView) :
        AnAction("Open in New Window", "Open in New Window", AllIcons.Actions.ChangeView) {
    private var count = 0//count of parser window
    override fun actionPerformed(event: AnActionEvent) {
        if (tabView?.getComponent() != null) {
            val parserWindow = JsonMasterWindow(tabView, "Json Master $count", count)
            count += 1
            parserWindow.windowAdapter = object : WindowAdapter() {
                override fun windowClosing(e: WindowEvent?) {
                    super.windowClosing(e)
                    if (count > 0) {
                        count -= 1
                    }
                }
            }
        }
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabled = count <= 10
    }
}