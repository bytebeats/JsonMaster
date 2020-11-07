package me.bytebeats.jsonmstr.ui.action

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import me.bytebeats.jsonmstr.log.LogUtil
import me.bytebeats.jsonmstr.ui.dialog.ParserDialog
import me.bytebeats.jsonmstr.ui.dialog.ParserDialog2
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/16 15:57
 * @Version 1.0
 * @Description OpenJsonMasterAction is Action to popup Json Master Plugin
 */

class OpenJsonMasterAction : AnAction("Json Master", "Parse in Json Master", AllIcons.Actions.Edit) {
    private var lastActionPerformedTime = 0L
    private var count = 0//count of parser window
    override fun actionPerformed(event: AnActionEvent) {
        if (isFastClick()) {
            return
        }
        openNewDialog(event)
    }

    private fun openNewDialog(event: AnActionEvent) {
        val editor = event.getData(PlatformDataKeys.EDITOR) ?: return
        val model = editor.selectionModel
        val text = model.selectedText
        event.presentation.isEnabled = !text.isNullOrEmpty()
        if (text.isNullOrEmpty()) {
            LogUtil.i("text is null or empty")
            return
        }
        val parserWindow = ParserDialog2(text, "Json Master $count", count)
        count += 1
        parserWindow.windowAdapter = object : WindowAdapter() {
            override fun windowClosing(e: WindowEvent?) {
                super.windowClosing(e)
                if (count > 0) {
                    count -= 1
                }
            }
        }
    }

    private fun isFastClick(timeSpan: Long = ACTION_TIME_SPAN): Boolean {
        val nowInMillis = System.currentTimeMillis()
        if (nowInMillis - lastActionPerformedTime < timeSpan) {
            return true
        }
        lastActionPerformedTime = nowInMillis
        return false
    }

    companion object {
        const val ACTION_TIME_SPAN = 500L
    }
}