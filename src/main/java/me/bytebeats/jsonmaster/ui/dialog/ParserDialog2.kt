package me.bytebeats.jsonmaster.ui.dialog

import com.intellij.openapi.application.ex.ApplicationManagerEx
import com.intellij.openapi.project.ex.ProjectManagerEx
import me.bytebeats.jsonmaster.ui.form.HorizontalTabWindow
import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.EventQueue
import java.awt.Toolkit
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import javax.swing.JFrame

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/16 15:23
 * @Version 1.0
 * @Description TO-DO
 */

class ParserDialog2(private val json: String, title: String, private val count: Int) : JFrame(title) {
    var windowAdapter: WindowAdapter? = null
    private val tabWindow by lazy { HorizontalTabWindow(ProjectManagerEx.getInstanceEx().defaultProject) }

    init {
        setupView()
        //Write access is allowed inside write-action only (see com.intellij.openapi.application.Application.runWriteAction())
        ApplicationManagerEx.getApplication().runWriteAction {
            tabWindow.setJson(json)
        }
    }

    private fun setupView() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        var width = screenSize.width * 2 / 5
        var height = screenSize.height * 2 / 5

        if (width == 0) {
            width = 650
        }
        if (height == 0) {
            height = 400
        }
        preferredSize = Dimension(width, height)
        add(tabWindow.provide(), BorderLayout.CENTER)
        size = Dimension(width, height)

        val x = screenSize.width / 2 - width / 2 + count * 20
        val y = screenSize.height / 2 - height / 2 + count * 20

        setLocation(x, y)
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                e?.window?.dispose()
                windowAdapter?.windowClosing(e)
            }
        })
        EventQueue.invokeLater {
            isVisible = true
            toFront()
            repaint()
        }
    }
}