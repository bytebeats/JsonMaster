package me.bytebeats.jsonmstr.ui

import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.Notification
import com.intellij.notification.NotificationListener
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowManager
import com.intellij.openapi.wm.ex.ToolWindowManagerEx
import com.intellij.openapi.wm.ex.ToolWindowManagerListener
import com.intellij.ui.content.Content
import com.intellij.ui.content.ContentFactory
import me.bytebeats.jsonmstr.log.LogUtil
import me.bytebeats.jsonmstr.ui.action.AddTabAction
import me.bytebeats.jsonmstr.ui.action.CloseTabAction
import me.bytebeats.jsonmstr.ui.action.NewParserDialogAction
import me.bytebeats.jsonmstr.ui.palette.ParserToolbarPanel
import me.bytebeats.jsonmstr.ui.tab.ITabView
import me.bytebeats.jsonmstr.ui.tab.TabView
import me.bytebeats.jsonmstr.util.Constants.Companion.PLUGIN_NAME
import javax.swing.SwingConstants

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/15 20:40
 * @Version 1.0
 * @Description JsonMasterComponent
 */

class JsonMasterWindow(private val project: Project) {
    private var time = 0L
    private var isShown = false;

    fun addContentTo(toolWindow: ToolWindow) {
        val content = createContentPanel(toolWindow)
        content.isCloseable = true
        toolWindow.contentManager.addContent(content)
        ((ToolWindowManager.getInstance(project)) as ToolWindowManagerEx).addToolWindowManagerListener(
            createToolWindowListener()
        )
    }

    private fun createContentPanel(toolWindow: ToolWindow): Content {
        toolWindow.setToHideOnEmptyContent(true)
        val panel = ParserToolbarPanel(PropertiesComponent.getInstance(project), toolWindow)
        val content = ContentFactory.SERVICE.getInstance().createContent(panel, "", false)
        val tabView = createTabContent(content)
        val toolbar = createToolbar(tabView)
        panel.toolbar = toolbar.component
        panel.setContent(tabView.getComponent())
        return content
    }

    private fun createTabContent(content: Content): ITabView {
        val tabView = TabView(project, content)
        tabView.createTabSession()
        return tabView
    }

    private fun createToolbar(tabView: ITabView): ActionToolbar {
        val group = DefaultActionGroup()

        group.add(AddTabAction(tabView))
        group.add(CloseTabAction(tabView))
        group.add(NewParserDialogAction(tabView))

        val toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, group, false)
        toolbar.setOrientation(SwingConstants.VERTICAL)
        return toolbar
    }

    private fun createToolWindowListener(): ToolWindowManagerListener {
        return object : ToolWindowManagerListener {
            override fun toolWindowsRegistered(ids: MutableList<String>) {
                super.toolWindowsRegistered(ids)
                LogUtil.i(ids.toString())
            }

            override fun stateChanged(toolWindowManager: ToolWindowManager) {
                super.stateChanged(toolWindowManager)
                val toolWindow = toolWindowManager.getToolWindow(PLUGIN_NAME)
                toolWindow?.apply {
                    if (isVisible && contentManager.contentCount == 0) {
                        addContentTo(this)
                    } else if (!isVisible) {
                        if (!isShown && (time <= 0 || System.currentTimeMillis() - 3_600_000L > time)) {
                            isShown = true
                            time = System.currentTimeMillis()
                            Notifications.Bus.notify(
                                Notification(
                                    PLUGIN_NAME,
                                    "Like it!",
                                    "Love Json Master? <a href=https://www.paypal.me/bytesbeat>Donate</a> or <b>Give it a star</b>  <a href=https://plugins.jetbrains.com/plugin/15218-json-master>Json Master</a> and spread the word!",
                                    NotificationType.INFORMATION,
                                    NotificationListener.UrlOpeningListener(true)
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    companion object {
        fun newInstance(project: Project): JsonMasterWindow {
            return project.getComponent(JsonMasterWindow::class.java)
        }
    }
}