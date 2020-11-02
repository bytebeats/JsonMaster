package me.bytebeats.jsonmstr.ui.form;

import com.google.gson.JsonSyntaxException;
import com.intellij.icons.AllIcons;
import com.intellij.ide.highlighter.HtmlFileHighlighter;
import com.intellij.ide.highlighter.HtmlFileType;
import com.intellij.ide.highlighter.XmlFileHighlighter;
import com.intellij.ide.highlighter.XmlFileType;
import com.intellij.json.JsonFileType;
import com.intellij.json.highlighting.JsonSyntaxHighlighterFactory;
import com.intellij.lang.Language;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.command.WriteCommandAction;
import com.intellij.openapi.editor.Document;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.EditorFactory;
import com.intellij.openapi.editor.EditorSettings;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.ex.EditorEx;
import com.intellij.openapi.editor.ex.util.LayerDescriptor;
import com.intellij.openapi.editor.ex.util.LayeredLexerEditorHighlighter;
import com.intellij.openapi.editor.highlighter.EditorHighlighter;
import com.intellij.openapi.fileTypes.*;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.psi.PsiDocumentManager;
import com.intellij.psi.PsiFile;
import com.intellij.psi.tree.IElementType;
import me.bytebeats.jsonmstr.intf.ComponentProvider;
import me.bytebeats.jsonmstr.meta.LineData;
import me.bytebeats.jsonmstr.ui.action.JMRadioAction;
import me.bytebeats.jsonmstr.util.Constants;
import me.bytebeats.jsonmstr.util.GsonUtil;
import me.bytebeats.jsonmstr.util.LineDataUtil;
import me.bytebeats.jsonmstr.util.TreeModelFactory;
import org.apache.commons.httpclient.Header;
import org.apache.http.util.TextUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/14 17:56
 * @Version 1.0
 * @Description TO-DO
 */

public class ParserStageView implements ComponentProvider {
    private Project mProject;
    private ComponentProvider mProvider;
    private JPanel stage_panel;
    private JPanel stage_content_panel;
    private JPanel stage_pretty_panel;
    private JPanel stage_compact_panel;
    private JScrollPane stage_tree_scroll_panel;
    private JTree stage_tree;
    private JPanel stage_tool_bar_panel;
    private SimpleToolWindowPanel stage_tool_bar;
    private JPanel stage_xml_panel;
    private JPanel stage_csv_panel;
    private JPanel stage_yaml_panel;

    private final CardLayout mPreviewCardLayout;
    private final Editor prettyEditor;
    private final Editor compactEditor;
    private final Editor xmlEditor;
    private final Editor csvEditor;
    private final Editor yamlEditor;

    public ParserStageView(Project mProject, ComponentProvider provider) {
        this.mProject = mProject;
        this.mProvider = provider;

        this.mPreviewCardLayout = (CardLayout) stage_content_panel.getLayout();
        prettyEditor = createEditor();
        compactEditor = createEditor();
        xmlEditor = createEditor();
        csvEditor = createEditor();
        yamlEditor = createEditor();

        stage_pretty_panel.add(prettyEditor.getComponent(), BorderLayout.CENTER);
        stage_compact_panel.add(compactEditor.getComponent(), BorderLayout.CENTER);
        stage_xml_panel.add(xmlEditor.getComponent(), BorderLayout.CENTER);
        stage_csv_panel.add(csvEditor.getComponent(), BorderLayout.CENTER);
        stage_yaml_panel.add(yamlEditor.getComponent(), BorderLayout.CENTER);
        updateTreeIcon();
        resetTree();
        createToolPanel();
    }

    private void createUIComponents() {
        createToolPanel();
    }

    public void parse(String rawJson) {
        parsePretty(rawJson);
        parseCompact(rawJson);
        parseTree(rawJson);
    }

    private void createToolPanel() {
        stage_tool_bar = new SimpleToolWindowPanel(true, true);
        final ButtonGroup buttonGroup = new ButtonGroup();
//        final AnAction[] actions = new AnAction[7];
        final AnAction[] actions = new AnAction[4];
        final ActionListener listener = e -> mPreviewCardLayout.show(stage_content_panel, e.getActionCommand());
        actions[0] = new JMRadioAction(Constants.PRETTY, Constants.PRETTY, buttonGroup, listener, true);
        actions[1] = new JMRadioAction(Constants.COMPACT, Constants.COMPACT, buttonGroup, listener);
        actions[2] = new JMRadioAction(Constants.TREE, Constants.TREE, buttonGroup, listener);
//        actions[3] = new JMRadioAction(Constants.XML, Constants.XML, buttonGroup, listener);
//        actions[4] = new JMRadioAction(Constants.CSV, Constants.CSV, buttonGroup, listener);
//        actions[5] = new JMRadioAction(Constants.YAML, Constants.YAML, buttonGroup, listener);
        actions[3] = new AnAction(Constants.USE_SOFT_WRAPS, Constants.USE_SOFT_WRAPS_DESC, AllIcons.Actions.ToggleSoftWrap) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                EventQueue.invokeLater(() -> {
                    try {
                        if (buttonGroup.getSelection() != null) {
                            String actionCommand = buttonGroup.getSelection().getActionCommand();
                            if (Constants.PRETTY.equals(actionCommand)) {
                                EditorSettings settings = prettyEditor.getSettings();
                                settings.setUseSoftWraps(!settings.isUseSoftWraps());
                            } else if (Constants.COMPACT.equals(actionCommand)) {
                                EditorSettings settings = compactEditor.getSettings();
                                settings.setUseSoftWraps(!settings.isUseSoftWraps());
                            } else if (Constants.XML.equals(actionCommand)) {
                                EditorSettings settings = xmlEditor.getSettings();
                                settings.setUseSoftWraps(!settings.isUseSoftWraps());
                            } else if (Constants.CSV.equals(actionCommand)) {
                                EditorSettings settings = csvEditor.getSettings();
                                settings.setUseSoftWraps(!settings.isUseSoftWraps());
                            } else if (Constants.YAML.equals(actionCommand)) {
                                EditorSettings settings = yamlEditor.getSettings();
                                settings.setUseSoftWraps(!settings.isUseSoftWraps());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        ActionGroup group = new DefaultActionGroup(actions);
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.TOOLBAR, group, true);
        stage_tool_bar.setToolbar(toolbar.getComponent());
        stage_tool_bar.setContent(new JPanel(new BorderLayout()));
    }

    private Editor createEditor() {
        EditorFactory factory = EditorFactory.getInstance();
        PsiFile file = null;//todo: persist raw json text
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

        ((EditorEx) editor).setHighlighter(createHighlighter(FileTypes.PLAIN_TEXT));
        return editor;
    }

    private EditorHighlighter createHighlighter(LanguageFileType fileType) {
        SyntaxHighlighter syntaxHighlighter = SyntaxHighlighterFactory.getSyntaxHighlighter(fileType, null, null);
        if (syntaxHighlighter == null) {
            syntaxHighlighter = new PlainSyntaxHighlighter();
        }
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        LayeredLexerEditorHighlighter layeredHighlighter = new LayeredLexerEditorHighlighter(getFileHighlighter(fileType), scheme);
        layeredHighlighter.registerLayer(new IElementType("TEXT", Language.ANY), new LayerDescriptor(syntaxHighlighter, ""));
        return layeredHighlighter;
    }

    private SyntaxHighlighter getFileHighlighter(FileType fileType) {
        if (HtmlFileType.INSTANCE == fileType) {
            return new HtmlFileHighlighter();
        } else if (fileType == XmlFileType.INSTANCE) {
            return new XmlFileHighlighter();
        } else if (fileType == JsonFileType.INSTANCE) {
            return JsonSyntaxHighlighterFactory.getSyntaxHighlighter(fileType, mProject, null);
        }
        return new PlainSyntaxHighlighter();
    }

    private LanguageFileType getFileType(Header[] contentTypes) {
        if (contentTypes != null && contentTypes.length > 0) {
            Header contentType = contentTypes[0];
            if (contentType.getValue().contains("text/html")) {
                return HtmlFileType.INSTANCE;
            } else if (contentType.getValue().contains("application/xml")) {
                return XmlFileType.INSTANCE;
            } else if (contentType.getValue().contains("application/json")) {
                return JsonFileType.INSTANCE;
            }
        }
        return JsonFileType.INSTANCE;
    }

    private void updateTreeIcon() {
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) stage_tree.getCellRenderer();
        Icon icon = new ImageIcon();
        renderer.setClosedIcon(icon);
        renderer.setOpenIcon(icon);
        renderer.setLeafIcon(icon);
    }

    private void resetTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        DefaultTreeModel model = new DefaultTreeModel(root);
        stage_tree.setModel(model);
    }

    private void expandAllNodes(JTree tree, int start, int rowCount) {
        for (int i = start; i < rowCount; i++) {
            tree.expandRow(i);
        }
        if (tree.getRowCount() != rowCount) {
            expandAllNodes(tree, rowCount, tree.getRowCount());
        }
    }

    @NotNull
    @Override
    public JComponent provide() {
        return mProvider.provide();
    }

    public JComponent getContainer() {
        return stage_panel;
    }

    private void parsePretty(String raw) {
        try {
            String prettyJson = getPrettyJson(raw);
            WriteCommandAction.runWriteCommandAction(mProject, () -> {
                Document document = prettyEditor.getDocument();
                document.setReadOnly(false);
                document.setText(prettyJson);
                document.setReadOnly(true);
            });
            ((EditorEx) prettyEditor).setHighlighter(createHighlighter(getFileType(null)));
        } catch (Exception e) {
            handleJsonSyntaxException(prettyEditor, e, raw);
        }
    }

    private void parseCompact(String raw) {
        if (raw == null) return;
        String compact = getCompactJson(raw);
        WriteCommandAction.runWriteCommandAction(mProject, () -> {
            Document document = compactEditor.getDocument();
            document.setReadOnly(false);
            document.setText(compact);
            document.setReadOnly(true);
        });
        ((EditorEx) compactEditor).setHighlighter(createHighlighter(getFileType(null)));
    }

    private void parseTree(String raw) {
        if (TextUtils.isEmpty(raw)) {
            resetTree();
            return;
        }
        try {
            String prettyJson = getPrettyJson(raw);
            DefaultTreeModel model = TreeModelFactory.INSTANCE.create(prettyJson);
            stage_tree.setModel(model);
            expandAllNodes(stage_tree, 0, stage_tree.getRowCount());
        } catch (Exception e) {
            e.printStackTrace();
            resetTree();
        }
    }

    private String getPrettyJson(String raw) throws RuntimeException {
        if (raw == null || raw.isEmpty()) return "";
        return GsonUtil.INSTANCE.toPrettyString(raw);
    }

    private String getCompactJson(String raw) {
        if (raw == null || raw.isEmpty()) return "";
        return raw.replaceAll("\t|\r|\n|\\s*", "");
    }

    private void handleJsonSyntaxException(Editor editor, Exception e, String raw) {
        if (e instanceof JsonSyntaxException) {
            String msg = e.getMessage();
            if (TextUtils.isEmpty(msg) && e.getCause() != null && !TextUtils.isEmpty(e.getCause().getMessage())) {
                msg = e.getCause().getMessage();
            }
            String finalMsg = msg;
            WriteCommandAction.runWriteCommandAction(mProject, () -> {
                Document document = editor.getDocument();
                document.setReadOnly(false);
                LineData lineData = LineDataUtil.INSTANCE.process(finalMsg);
                if (lineData == null) {
                    document.setText(raw);
                } else {
                    document.setText(raw + "\n\n\n" + e.getClass().getSimpleName() + " in line " + lineData.getNumber() + ":" + lineData.getOffset());
                }
                document.setReadOnly(true);
            });
            ((EditorEx) editor).setHighlighter(createHighlighter(getFileType(null)));
        }
    }
}
