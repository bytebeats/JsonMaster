package me.bytebeats.jsonmstr.ui.form;

import com.intellij.openapi.project.Project;

import javax.swing.*;

/**
 * @Author bytebeats
 * @Email <happychinapc@gmail.com>
 * @Github https://github.com/bytebeats
 * @Created on 2020/10/14 17:56
 * @Version 1.0
 * @Description TO-DO
 */

public class ParsedJsonView {
    private Project mProject;
    private ParserTabView mTabView;
    private JPanel parsed_panel;

    public ParsedJsonView(Project mProject, ParserTabView mTabView) {
        this.mProject = mProject;
        this.mTabView = mTabView;
    }

    public void parse(String rawJson) {

    }

    public JComponent getComponent() {
        return parsed_panel;
    }
}
