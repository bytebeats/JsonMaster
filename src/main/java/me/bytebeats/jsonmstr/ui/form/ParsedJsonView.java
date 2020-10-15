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
import me.bytebeats.jsonmstr.ui.action.JRadioAction;
import me.bytebeats.jsonmstr.util.GsonUtil;
import me.bytebeats.jsonmstr.util.StringUtil;
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

public class ParsedJsonView implements ComponentProvider {
    private Project mProject;
    private ParserTabView mTabView;
    private JPanel parsed_panel;
    private JPanel parsed_type_panel;
    private JPanel parsed_content_panel;
    private JPanel parsed_pretty_content_panel;
    private JPanel parsed_raw_content_panel;
    private JScrollPane parsed_tree_content_scroll_panel;
    private JTree parsed_tree;
    private SimpleToolWindowPanel parsed_tool_window_panel;

    private CardLayout mPreviewCardLayout;
    private Editor prettyEditor;
    private Editor rawEditor;

    public ParsedJsonView(Project mProject, ParserTabView mTabView) {
        this.mProject = mProject;
        this.mTabView = mTabView;

        this.mPreviewCardLayout = (CardLayout) parsed_content_panel.getLayout();
        prettyEditor = createEditor();
        rawEditor = createEditor();

        parsed_pretty_content_panel.add(prettyEditor.getComponent(), BorderLayout.CENTER);
        parsed_raw_content_panel.add(rawEditor.getComponent(), BorderLayout.CENTER);
        updateTreeIcon();
        resetTree();
        createToolPanel();
    }

    private void createUIComponents() {
        createToolPanel();
    }

    public void parse(String rawJson) {
        parsePretty(rawJson);
        parseRaw(rawJson);
        parseTree(rawJson);
    }

    private void createToolPanel() {
        parsed_tool_window_panel = new SimpleToolWindowPanel(true, true);
        final ButtonGroup buttonGroup = new ButtonGroup();
        final AnAction[] actions = new AnAction[4];
        final ActionListener listener = e -> mPreviewCardLayout.show(parsed_content_panel, e.getActionCommand());
        actions[0] = new JRadioAction("Pretty", "Pretty", buttonGroup, listener, true);
        actions[1] = new JRadioAction("Raw", "Raw", buttonGroup, listener);
//        actions[1] = new JRadioAction("Compact", "Compact", buttonGroup, listener);
//        actions[1] = new JRadioAction("Xml", "Xml", buttonGroup, listener);
//        actions[1] = new JRadioAction("Yaml", "Yaml", buttonGroup, listener);
        actions[2] = new JRadioAction("Tree", "Tree", buttonGroup, listener);
        actions[3] = new AnAction("Use Soft Wraps", "Toggle using soft wraps in current editor", AllIcons.Actions.ToggleSoftWrap) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
                EventQueue.invokeLater(() -> {
                    try {
                        if (buttonGroup.getSelection() != null) {
                            String actionCommand = buttonGroup.getSelection().getActionCommand();
                            if ("Pretty".equals(actionCommand)) {
                                EditorSettings settings = prettyEditor.getSettings();
                                settings.setUseSoftWraps(!settings.isUseSoftWraps());
                            } else if ("Raw".equals(actionCommand)) {
                                EditorSettings settings = rawEditor.getSettings();
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
        parsed_tool_window_panel.setToolbar(toolbar.getComponent());
        parsed_tool_window_panel.setContent(new JPanel(new BorderLayout()));
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
        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) parsed_tree.getCellRenderer();
        Icon icon = new ImageIcon();
        renderer.setClosedIcon(icon);
        renderer.setOpenIcon(icon);
        renderer.setLeafIcon(icon);
    }

    private void resetTree() {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("");
        DefaultTreeModel model = new DefaultTreeModel(root);
        parsed_tree.setModel(model);
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
    public Component provide() {
        return mTabView.provide();
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
            if (e instanceof JsonSyntaxException) {
                String msg = e.getMessage();
                if (TextUtils.isEmpty(msg) && e.getCause() != null && TextUtils.isEmpty(e.getCause().getMessage())) {
                    msg = e.getCause().getMessage();
                }
                String finalMsg = msg;
                WriteCommandAction.runWriteCommandAction(mProject, () -> {
                    Document document = prettyEditor.getDocument();
                    document.setReadOnly(false);
                    LineData lineData = StringUtil.INSTANCE.process(raw, finalMsg);
                    if (lineData == null) {
                        document.setText(raw);
                    } else {
                        document.setText(raw + "\n\n\n" + "Error in line " + lineData.getNumber() + ":" + lineData.getOffset());
                    }
                    document.setReadOnly(true);
                });
                ((EditorEx) prettyEditor).setHighlighter(createHighlighter(getFileType(null)));
            }
        }
    }

    private void parseRaw(String raw) {
        if (raw == null) return;
        WriteCommandAction.runWriteCommandAction(mProject, () -> {
            Document document = rawEditor.getDocument();
            document.setReadOnly(false);
            document.setText(raw);
            document.setReadOnly(true);
        });
        ((EditorEx) prettyEditor).setHighlighter(createHighlighter(getFileType(null)));
    }

    private void parseTree(String raw) {
        if (TextUtils.isEmpty(raw)) {
            resetTree();
            return;
        }
        try {
            String prettyJson = getPrettyJson(raw);
            DefaultTreeModel model = TreeModelFactory.INSTANCE.create(prettyJson);
            parsed_tree.setModel(model);
            expandAllNodes(parsed_tree, 0, parsed_tree.getRowCount());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getPrettyJson(String raw) throws Exception {
        if (raw == null || raw.isEmpty()) return "";
        return GsonUtil.INSTANCE.toPrettyString(raw);
    }
}
