package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.model.Material;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.Project;
import com.tsoft.myprocad.model.ProjectItemType;

import java.io.File;

public class ProjectBinding implements JavaScriptBinding {
    private Project project;

    public ProjectBinding(Project project) { this.project = project; }

    public MaterialBinding addMaterial(String name, float density, float price) {
        Material material = new Material();
        material.setName(name);
        material.setDensity(density);
        material.setPrice(price);
        project.addMaterial(material);

        return new MaterialBinding(material);
    }

    public PlanBinding addPlan(String name) {
        Plan plan = (Plan)project.addItem(ProjectItemType.PLAN, name, 0);
        return new PlanBinding(plan);
    }

    public void saveToFile(String fileName) {
        String userHome = System.getProperty("user.home") + File.separator;
        project.setFileName(userHome + fileName);
        project.projectController.save();
    }

    @Override
    public String getBindingName() {
        return "project";
    }
}
