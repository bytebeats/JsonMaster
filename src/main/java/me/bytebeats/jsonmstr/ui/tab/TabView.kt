package me.bytebeats.jsonmstr.ui.tab

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBPanel
import me.bytebeats.jsonmstr.intf.OnLastTabListener
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

class TabView(project: Project, disposable: Disposable) : JPanel(BorderLayout()), ITabView {
    private val mPanel by lazy { JBPanel<JBPanel<*>>(BorderLayout()) }
    private val mTabs by lazy {
        TabLayout(project, disposable).apply {
            setOnLastTabListener(object : OnLastTabListener {
                override fun onLast() {
                }
            })
            add(getComponent(), BorderLayout.CENTER)
        }
    }

    init {
        mPanel.add(this, BorderLayout.CENTER)
    }

    override fun createTabSession() {
    }

    override fun closeCurrentTabSession() {
        TODO("Not yet implemented")
    }

    override fun getTabCount(): Int {
        TODO("Not yet implemented")
    }

    override fun getComponent(): JComponent {
        TODO("Not yet implemented")
    }

    override fun newComponent(): JComponent {
        TODO("Not yet implemented")
    }
}