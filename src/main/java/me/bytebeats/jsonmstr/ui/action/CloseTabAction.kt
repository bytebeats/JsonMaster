package me.bytebeats.jsonmstr.ui.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import me.bytebeats.jsonmstr.log.Logger
import me.bytebeats.jsonmstr.ui.tab.ITabView
import me.bytebeats.jsonmstr.ui.tab.TabView
import org.jetbrains.debugger.getClassName

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/16 11:50
 * @Version 1.0
 * @Description TO-DO
 */

class CloseTabAction(private val tabView: ITabView) :
        AnAction("Close Tab", "Close Selected Json Master Tab", AllIcons.General.Remove) {
    override fun actionPerformed(event: AnActionEvent) {
        tabView.closeCurrentTabSession()
        Logger.i("${javaClass.simpleName}:actionPerformed")
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabled = tabView.getTabCount() > 0
    }
}