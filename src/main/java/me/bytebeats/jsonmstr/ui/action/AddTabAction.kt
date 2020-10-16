package me.bytebeats.jsonmstr.ui.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import me.bytebeats.jsonmstr.log.Logger
import me.bytebeats.jsonmstr.ui.tab.ITabView
import me.bytebeats.jsonmstr.util.Constants

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/16 11:41
 * @Version 1.0
 * @Description TO-DO
 */

class AddTabAction(private val tabView: ITabView) :
        AnAction(Constants.ADD_TAB, Constants.ADD_TAB_DESC, AllIcons.General.Add) {
    override fun actionPerformed(event: AnActionEvent) {
        tabView.createTabSession()
        Logger.i("${javaClass.simpleName}:actionPerformed")
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabled = tabView.getTabCount() <= 10
    }
}