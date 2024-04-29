package me.bytebeats.jsonmaster.ui.form;

import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import me.bytebeats.jsonmaster.intf.ComponentProvider;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/14 16:13
 * @Version 1.0
 * @Description VerticalTabWindow to parse json string into json/raw/tree style vertically
 */

public class VerticalTabWindow implements ComponentProvider {
    private Project mProject;
    private Editor mInputEditor;

    private JPanel v_tab_panel;
    private JSplitPane v_tab_split_pane;
    private JPanel v_tab_raw_panel;
    private JPanel v_tab_parsed_panel;
    private JPanel v_tab_raw_input_panel;
    private JButton v_tab_parse_btn;
    private ParserStageView mParserStageView;

    public VerticalTabWindow(Project project) {
        this.mProject = project;
        this.mInputEditor = createEditor();
        this.mParserStageView = new ParserStageView(mProject, this);
        this.v_tab_raw_input_panel.add(mInputEditor.getComponent(), BorderLayout.CENTER);
        this.v_tab_parsed_panel.add(mParserStageView.getContainer(), BorderLayout.CENTER);
        this.v_tab_parse_btn.addActionListener(e -> parse());
        this.mInputEditor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void beforeDocumentChange(@NotNull DocumentEvent event) {

            }

            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                // TODO: 2020/10/14 parse when raw json changed
            }
        });
    }

    private Editor createEditor() {//todo: persist raw json text
        EditorFactory factory = EditorFactory.getInstance();
        PsiFile file = null;

        Document document = file == null
                ? factory.createDocument("")
                : PsiDocumentManager.getInstance(mProject).getDocument(file);
        Editor editor = factory.createEditor(document, mProject);
        EditorSettings settings = editor.getSettings();
        settings.setVirtualSpace(false);
        settings.setLineMarkerAreaShown(false);
        settings.setIndentGuidesShown(false);
        settings.setFoldingOutlineShown(true);
        settings.setAdditionalColumnsCount(3);
        settings.setAdditionalLinesCount(3);
        settings.setLineNumbersShown(true);
        settings.setCaretRowShown(true);
        settings.setUseSoftWraps(true);
        return editor;
    }

    @NotNull
    @Override
    public JComponent provide() {
        return v_tab_panel;
    }

    private void parse() {
        String text = mInputEditor.getDocument().getText();
        if (text.isEmpty()) {
            return;
        }
        mParserStageView.parse(text);
    }
}
