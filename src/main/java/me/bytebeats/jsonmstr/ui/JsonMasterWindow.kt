package me.bytebeats.jsonmstr.ui

import me.bytebeats.jsonmstr.ui.tab.ITabView
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

class JsonMasterWindow(private val tabView: ITabView, title: String, private var count: Int) : JFrame() {
    var windowAdapter: WindowAdapter? = null

    init {
        setTitle(title)
        setupView()
    }

    private fun setupView() {
        val screenSize = Toolkit.getDefaultToolkit().screenSize
        var width = screenSize.width / 3
        var height = screenSize.height / 4

        if (width == 0) {
            width = 400
        }
        if (height == 0) {
            height = 250
        }
        preferredSize = Dimension(width, height)
        add(tabView.newComponent().apply {
        }, BorderLayout.CENTER)
        size = Dimension(width, height)

        val x = screenSize.width / 2 - width / 2 + count * 20
        val y = screenSize.height / 2 - height / 2 + count * 20

        setLocation(x, y)
        addWindowListener(object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
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