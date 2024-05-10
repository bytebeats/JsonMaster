package me.bytebeats.jsonmaster.ui

import com.intellij.ide.util.PropertiesComponent
import com.intellij.notification.BrowseNotificationAction
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
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
import me.bytebeats.jsonmaster.util.LogUtil
import me.bytebeats.jsonmaster.ui.action.AddTabAction
import me.bytebeats.jsonmaster.ui.action.CloseTabAction
import me.bytebeats.jsonmaster.ui.action.NewParserDialogAction
import me.bytebeats.jsonmaster.ui.palette.ParserToolbarPanel
import me.bytebeats.jsonmaster.ui.tab.ITabView
import me.bytebeats.jsonmaster.ui.tab.TabView
import me.bytebeats.jsonmaster.util.Constants.Companion.PLUGIN_NAME
import javax.swing.SwingConstants

/**
 * @author bytebeats
 * @email <happychinapc@gmail.com>
 * @github https://github.com/bytebeats
 * @created on 2020/10/15 20:40
 * @version 1.0
 * @description JsonMasterComponent
 */

class JsonMasterWindow(private val project: Project) {
    private var time = 0L
    private var isShown = false

    fun addContentTo(toolWindow: ToolWindow) {
        val content = createContentPanel(toolWindow)
        content.isCloseable = true
        toolWindow.contentManager.addContent(content)
        ToolWindowManagerEx.getInstanceEx(project).apply {

//            addToolWindowManagerListener(
//                createToolWindowListener()
//            )
        }
    }

    private fun createContentPanel(toolWindow: ToolWindow): Content {
        toolWindow.setToHideOnEmptyContent(true)
        val panel = ParserToolbarPanel(PropertiesComponent.getInstance(project), toolWindow)
        val content = ContentFactory.getInstance().createContent(panel, "", false)
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
        toolbar.targetComponent = tabView as TabView
        toolbar.setOrientation(SwingConstants.VERTICAL)
        return toolbar
    }

    private fun createToolWindowListener(): ToolWindowManagerListener {
        return object : ToolWindowManagerListener {
            override fun toolWindowsRegistered(ids: MutableList<String>, toolWindowManager: ToolWindowManager) {
                ids.forEach { id ->
                    LogUtil.i("toolWindowRegistered: $id")
                }
            }

            override fun stateChanged(
                toolWindowManager: ToolWindowManager,
                changeType: ToolWindowManagerListener.ToolWindowManagerEventType
            ) {
                val toolWindow = ToolWindowManagerEx.getInstanceEx(project).getToolWindow(PLUGIN_NAME)
                toolWindow?.apply {
                    if (isVisible && contentManager.contentCount == 0) {
                        addContentTo(this)
                    } else if (!isVisible) {
                        if (!isShown && (time <= 0 || System.currentTimeMillis() - 3_600_000L > time)) {
                            isShown = true
                            time = System.currentTimeMillis()
                            NotificationGroupManager.getInstance()
                                .getNotificationGroup(PLUGIN_NAME)
                                .createNotification(
                                    "Like it!",
                                    "Love Json Master? <b>Donate</b> or <b>Give it a star</b> Json Master and spread the word!",
                                    NotificationType.INFORMATION
                                )
                                .addAction(
                                    BrowseNotificationAction("Donate", "https://www.paypal.me/bytesbeat")
                                )
                                .addAction(
                                    BrowseNotificationAction(
                                        "Json Master",
                                        "https://plugins.jetbrains.com/plugin/15218-json-master"
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