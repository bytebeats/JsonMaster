package me.bytebeats.jsonmaster.ui.action

import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Presentation
import com.intellij.openapi.actionSystem.ex.CustomComponentAction
import com.intellij.openapi.util.Key
import com.intellij.util.ui.UIUtil
import java.awt.event.ActionListener
import javax.swing.ButtonGroup
import javax.swing.JComponent
import javax.swing.JRadioButton

/**
 * @author bytebeats
 * @email <happychinapc@gmail.com>
 * @github https://github.com/bytebeats
 * @created on 2020/10/15 16:21
 * @version 1.0
 * @description JRadioAction is JRadioButton with an Action
 */

class JMRadioAction @JvmOverloads constructor(
    text: String? = null,
    private val actionCommand: String? = null,
    private val buttonGroup: ButtonGroup? = null,
    private val actionListener: ActionListener? = null,
    private val selected: Boolean = true
) : AnAction(text), CustomComponentAction {

    private val keyName = "selected"

    override fun actionPerformed(e: AnActionEvent) {

    }

    override fun createCustomComponent(presentation: Presentation, place: String): JComponent {
        val radioBtn = JRadioButton("")
        radioBtn.addActionListener { e ->
            val btn = e.source as JRadioButton
            val actionToolbar = UIUtil.getParentOfType(ActionToolbar::class.java, btn)
            val dataContext = actionToolbar?.toolbarDataContext ?: DataManager.getInstance().getDataContext(btn)
            this@JMRadioAction.actionPerformed(
                AnActionEvent.createFromAnAction(
                    this@JMRadioAction,
                    null,
                    place,
                    dataContext
                )
            )
            actionListener?.actionPerformed(e)
        }
        presentation.putClientProperty(Key(keyName), selected)
        updateCustomComponent(radioBtn, presentation)
        return radioBtn
    }

    private fun updateCustomComponent(radioButton: JRadioButton, presentation: Presentation) {
        radioButton.text = presentation.text
        radioButton.toolTipText = presentation.description
        radioButton.mnemonic = presentation.mnemonic
        radioButton.displayedMnemonicIndex = presentation.displayedMnemonicIndex
        radioButton.isSelected = true == presentation.getClientProperty(Key(keyName))
        radioButton.isEnabled = true
        radioButton.isVisible = presentation.isVisible
        if (!actionCommand.isNullOrEmpty()) {
            radioButton.actionCommand = actionCommand
        }
        buttonGroup?.apply { add(radioButton) }
    }
}