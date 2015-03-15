package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.model.Project;

public class ProjectBinding implements JavaScriptBinding {
    private Project project;

    public ProjectBinding(Project project) { this.project = project; }

    @Override
    public String getBindingName() {
        return "project";
    }
}
