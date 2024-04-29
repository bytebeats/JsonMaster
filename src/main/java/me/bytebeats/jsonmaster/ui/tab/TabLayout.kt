package me.bytebeats.jsonmaster.ui.tab

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.TabsListener
import com.intellij.ui.tabs.impl.JBEditorTabs
import me.bytebeats.jsonmaster.log.LogUtil
import javax.swing.JComponent

/**
 * @author bytebeats
 * @email <happychinapc@gmail.com>
 * @github https://github.com/bytebeats
 * @created on 2020/10/13 20:26
 * @version 1.0
 * @description TabLayout
 */

class TabLayout(private val project: Project, private val parent: Disposable) : ITabLayout {
    private val mTabs by lazy {
        JBEditorTabs(project, parent)
            .apply {
                addListener(createTabsListener())
                setTabDraggingEnabled(true)
            }
    }

    override fun addTab(component: JComponent, title: String): ITabLayout {
        val tab = TabInfo(component).apply { text = title }
        mTabs.addTab(tab)
        mTabs.select(tab, true)
        return this
    }

    override fun getTabCount(): Int = mTabs.tabCount

    override fun getTabAt(position: Int): TabInfo = mTabs.getTabAt(position)

    override fun closeTab(position: Int): ITabLayout {
        if (position in 0 until mTabs.tabCount) {
            mTabs.removeTab(mTabs.getTabAt(position))
        }
        return this
    }

    override fun closeCurrentTab(): ITabLayout {
        mTabs.removeTab(mTabs.selectedInfo)
        return this
    }

    override fun getComponent(): JBEditorTabs = mTabs

    override fun getTitleAt(position: Int): String = mTabs.getTabAt(position).text

    override fun getCurrentTabComponent(): JComponent = mTabs.selectedInfo!!.component

    private fun createTabsListener(): TabsListener = object : TabsListener {
        override fun selectionChanged(oldSelection: TabInfo?, newSelection: TabInfo?) {
            LogUtil.i("On Tab selection changed: ${oldSelection?.text} to ${newSelection?.text}")
        }

        override fun beforeSelectionChanged(oldSelection: TabInfo?, newSelection: TabInfo?) {
            LogUtil.i("On before Tab selection changed: ${oldSelection?.text} to ${newSelection?.text}")
        }

        override fun tabRemoved(tabToRemove: TabInfo) {
            LogUtil.i("On tabRemoved: ${tabToRemove.text}")
        }

        override fun tabsMoved() {
            LogUtil.i("On tabsMoved")
        }
    }
}