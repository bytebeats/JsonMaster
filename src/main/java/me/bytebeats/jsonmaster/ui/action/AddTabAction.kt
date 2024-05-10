package me.bytebeats.jsonmaster.ui.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import me.bytebeats.jsonmaster.util.LogUtil
import me.bytebeats.jsonmaster.ui.tab.ITabView
import me.bytebeats.jsonmaster.util.Constants

/**
 * @author bytebeats
 * @email <happychinapc@gmail.com>
 * @github https://github.com/bytebeats
 * @created on 2020/10/16 11:41
 * @version 1.0
 * @description AddTabAction is an Action to add a ParserTabView into the
 *     window of Json Master Plugin
 */

class AddTabAction(private val tabView: ITabView) :
    AnAction(Constants.ADD_TAB, Constants.ADD_TAB_DESC, AllIcons.General.Add) {
    override fun actionPerformed(event: AnActionEvent) {
        tabView.createTabSession()
        LogUtil.i("AddTabAction:actionPerformed")
    }

    override fun update(e: AnActionEvent) {
        super.update(e)
        e.presentation.isEnabled = tabView.getTabCount() <= 10
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.EDT
    }
}