package me.bytebeats.jsonmstr.ui.tab

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.ui.tabs.TabInfo
import com.intellij.ui.tabs.TabsListener
import com.intellij.ui.tabs.impl.JBEditorTabs
import me.bytebeats.jsonmstr.intf.OnLastTabListener
import me.bytebeats.jsonmstr.log.Logger
import javax.swing.JComponent

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/13 20:26
 * @Version 1.0
 * @Description TabLayout
 */

class TabLayout(private val project: Project, private val parent: Disposable) : ITabLayout {
    private val mTabs by lazy {
        JBEditorTabs(project, IdeFocusManager.getInstance(project), parent)
            .apply {
                addListener(createTabsListener())
                isTabDraggingEnabled = true
            }
    }

    private var mLastTabListener: OnLastTabListener? = null

    override fun setOnLastTabListener(listener: OnLastTabListener): ITabLayout {
        mLastTabListener = listener
        return this
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
            Logger.i("On Tab selection change: ${oldSelection?.text} to ${newSelection?.text}")
        }

        override fun beforeSelectionChanged(oldSelection: TabInfo?, newSelection: TabInfo?) {
            Logger.i("On before Tab selection change: ${oldSelection?.text} to ${newSelection?.text}")
        }

        override fun tabRemoved(tabToRemove: TabInfo) {
            mLastTabListener?.apply {
                if (getTabCount() == 1) {
                    onLast()
                }
            }
        }

        override fun tabsMoved() {
            Logger.i("On Tabs moved")
        }
    }
}