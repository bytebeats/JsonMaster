package me.bytebeats.jsonmstr.ui.action

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import me.bytebeats.jsonmstr.log.Logger

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/16 15:57
 * @Version 1.0
 * @Description TO-DO
 */

class OpenJsonMasterAction : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        val editor = event.getData(PlatformDataKeys.EDITOR)
        if (editor == null) return
        val model = editor.selectionModel
        val text = model.selectedText
        if (text.isNullOrEmpty()) {
            Logger.i("text is null or empty")
            return
        }
        Logger.i("text is: $text")
    }
}