package me.bytebeats.jsonmstr.ui.action

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.actionSystem.ex.CustomComponentAction
import com.intellij.openapi.diagnostic.LogUtil
import com.intellij.ui.components.JBRadioButton
import com.intellij.util.ui.UIUtil
import me.bytebeats.jsonmstr.log.Logger
import java.awt.event.ActionListener
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.JRadioButton

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/15 16:21
 * @Version 1.0
 * @Description TO-DO
 */

class JRadioAction @JvmOverloads constructor(
    val text: String? = null,
    val actionCommand: String? = null,
    val buttonGroup: ButtonGroup? = null,
    val actionListener: ActionListener? = null,
    val selected: Boolean = true
) : AnAction(text), CustomComponentAction {
    override fun actionPerformed(e: AnActionEvent) {

    }

    override fun createCustomComponent(presentation: Presentation, place: String): JComponent {
        val radioBtn = JRadioButton("")
        radioBtn.addActionListener { e ->
            val btn = e.source as JRadioButton
            val actionToolbar = UIUtil.getParentOfType(ActionToolbar::class.java, btn)
            val dataContext = actionToolbar?.toolbarDataContext ?: DataManager.getInstance().getDataContext(btn)
            this@JRadioAction.actionPerformed(
                AnActionEvent.createFromAnAction(
                    this@JRadioAction,
                    null,
                    "unknown",
                    dataContext
                )
            )
            Logger.i("JRadioAction.createCustomComponent")
            actionListener?.actionPerformed(e)
        }
        presentation.putClientProperty("selected", selected)
        updateCustomComponent(radioBtn, presentation)
        return radioBtn
    }

    private fun updateCustomComponent(radioButton: JRadioButton, presentation: Presentation) {
        radioButton.text = presentation.text
        radioButton.toolTipText = presentation.description
        radioButton.mnemonic = presentation.mnemonic
        radioButton.displayedMnemonicIndex = presentation.displayedMnemonicIndex
        radioButton.isSelected = true == presentation.getClientProperty("selected")
//        radioButton.isEnabled = presentation.isEnabled
        radioButton.isEnabled = true
        radioButton.isVisible = presentation.isVisible
        if (!actionCommand.isNullOrEmpty()) {
            radioButton.actionCommand = actionCommand
        }
        buttonGroup?.apply { add(radioButton) }
    }
}