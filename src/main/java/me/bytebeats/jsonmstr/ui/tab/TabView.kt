package me.bytebeats.jsonmstr.ui.tab

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import me.bytebeats.jsonmstr.intf.OnLastTabListener
import me.bytebeats.jsonmstr.ui.form.ParserTabView
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/13 20:55
 * @Version 1.0
 * @Description TO-DO
 */

class TabView(private val project: Project, private val disposable: Disposable) : JPanel(BorderLayout()), ITabView {
    private val mPanel by lazy { JBPanel<JBPanel<*>>(BorderLayout()) }
    private val mTabs by lazy {
        TabLayout(project, disposable).apply {
            setOnLastTabListener(object : OnLastTabListener {
                override fun onLast() {
                }
            })
            add(this.getComponent(), BorderLayout.CENTER)
        }
    }

    init {
        mPanel.add(this, BorderLayout.CENTER)
    }

    private fun addTab(component: JComponent, tabLayout: ITabLayout) {
        mTabs.addTab(component, generateTabName(tabLayout))
    }

    private fun generateTabName(tabLayout: ITabLayout): String {
        val titles = mutableSetOf<String>()
        for (i in 0 until tabLayout.getTabCount()) {
            titles.add(tabLayout.getTitleAt(i))
        }
        val suggestedTitle = "Parser"
        var newTabTitle = suggestedTitle
        var i = 0
        while (titles.contains(newTabTitle)) {
            newTabTitle = "$newTabTitle (${++i})"
        }
        return newTabTitle
    }

    override fun createTabSession() {
        addTab(createParserTabView(), mTabs)
    }

    override fun closeCurrentTabSession() {
        mTabs.closeCurrentTab()
    }

    override fun getTabCount(): Int = mTabs.getTabCount()

    override fun getComponent(): JComponent = mPanel

    override fun newComponent(): JComponent = createParserTabView()

    private fun createParserTabView(): JComponent{
        return ParserTabView(project, disposable).provide()
    }
}