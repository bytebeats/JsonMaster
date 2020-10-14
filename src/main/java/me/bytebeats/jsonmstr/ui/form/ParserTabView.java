package me.bytebeats.jsonmstr.ui.form;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/14 16:13
 * @Version 1.0
 * @Description TO-DO
 */

public class ParserTabView {
    private Project mProject;
    private Disposable mParent;
    private Editor mInputEditor;

    private JPanel v_tab_panel;
    private JSplitPane v_tab_split_pane;
    private JPanel v_tab_raw_panel;
    private JPanel v_tab_parsed_panel;
    private JPanel v_tab_raw_input_panel;
    private JButton v_tab_parse_btn;
    private ParsedJsonView mParsedJsonView;

    private SplitOrientation mOrientation;

    private String mRawJsonText = "";

    public ParserTabView(Project project, Disposable disposable) {
        this.mProject = project;
        this.mParent = disposable;
        this.mInputEditor = createEditor();
        mOrientation = SplitOrientation.VERTICAL;
        v_tab_split_pane.setOrientation(mOrientation.value);
        updateSplitPane();
        this.mParsedJsonView = new ParsedJsonView(mProject, this);
        this.v_tab_raw_input_panel.add(mInputEditor.getComponent(), BorderLayout.CENTER);
        this.v_tab_parsed_panel.add(mParsedJsonView.getComponent(), BorderLayout.CENTER);
        this.v_tab_parse_btn.addActionListener(e -> parse());
        this.mInputEditor.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void beforeDocumentChange(@NotNull DocumentEvent event) {

            }

            @Override
            public void documentChanged(@NotNull DocumentEvent event) {
                // TODO: 2020/10/14 parse when raw json changed
            }

            @Override
            public void bulkUpdateStarting(@NotNull Document document) {

            }

            @Override
            public void bulkUpdateFinished(@NotNull Document document) {

            }
        });
    }

    private Editor createEditor() {//todo: persist raw json text
        EditorFactory factory = EditorFactory.getInstance();
        Document document = factory.createDocument("");
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

    public void setOrientation(SplitOrientation orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            v_tab_split_pane.setOrientation(mOrientation.value);
            updateSplitPane();
        }
    }

    private void updateSplitPane() {
        if (v_tab_split_pane.getOrientation() == SplitOrientation.VERTICAL.value) {
            v_tab_split_pane.setDividerLocation(132);
        } else {
            v_tab_split_pane.setDividerLocation(300);
        }
    }

    public JComponent getComponent() {
        return v_tab_panel;
    }

    private void parse() {
        String text = mInputEditor.getDocument().getText();
        if (text.isEmpty() || mRawJsonText.equals(text)) {
            return;
        }
        mRawJsonText = text;
        mParsedJsonView.parse(mRawJsonText);
    }

    public enum SplitOrientation {
        VERTICAL(0), HORIZONTAL(1);
        private final int value;

        SplitOrientation(int value) {
            this.value = value;
        }
    }
}
