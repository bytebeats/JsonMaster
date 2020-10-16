package me.bytebeats.jsonmstr.ui.form;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.event.DocumentEvent;
import com.intellij.openapi.editor.event.DocumentListener;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import me.bytebeats.jsonmstr.intf.ComponentProvider;
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

public class ParserTabView implements ComponentProvider {
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

    public ParserTabView(Project project, Disposable disposable) {
        this.mProject = project;
        this.mParent = disposable;
        this.mInputEditor = createEditor();
        mOrientation = SplitOrientation.VERTICAL;
        v_tab_split_pane.setOrientation(mOrientation.value);
        updateSplitPane();
        this.mParsedJsonView = new ParsedJsonView(mProject, this);
        this.v_tab_raw_input_panel.add(mInputEditor.getComponent(), BorderLayout.CENTER);
        this.v_tab_parsed_panel.add(mParsedJsonView.getContainer(), BorderLayout.CENTER);
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

    public void setOrientation(SplitOrientation orientation) {
        if (mOrientation != orientation) {
            mOrientation = orientation;
            v_tab_split_pane.setOrientation(mOrientation.value);
            updateSplitPane();
        }
    }

    private void updateSplitPane() {
        if (v_tab_split_pane.getOrientation() == SplitOrientation.VERTICAL.value) {
            v_tab_split_pane.setDividerLocation(200);
        } else {
            v_tab_split_pane.setDividerLocation(300);
        }
    }

    @NotNull
    @Override
    public JComponent provide() {
        return v_tab_panel;
    }

    private void parse() {
        String text = mInputEditor.getDocument().getText();
        mParsedJsonView.parse(text);
    }

    public enum SplitOrientation {
        VERTICAL(0), HORIZONTAL(1);
        private final int value;

        SplitOrientation(int value) {
            this.value = value;
        }
    }
}
