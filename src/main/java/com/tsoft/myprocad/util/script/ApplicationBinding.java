package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.model.Project;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.viewcontroller.ApplicationController;
import com.tsoft.myprocad.viewcontroller.ProjectController;

import javax.script.ScriptException;
import java.io.IOException;

public class ApplicationBinding {
    private JavaScript js;

    public ApplicationBinding(JavaScript js) {
        this.js = js;
    }

    public Project createProject() {
        MyProCAD myProCAD = new MyProCAD();
        myProCAD.init();

        Menu.NEW_PROJECT.doAction();

        ProjectController projectController = ApplicationController.getInstance().getActiveProjectController();
        return projectController.project;
    }

    public void executeScript(String fileName) throws IOException, ScriptException {
        js.executeScript(fileName);
    }

 /*   public void saveToFile(String fileName) {
        String userHome = System.getProperty("user.home") + File.separator;
        project.setFileName(userHome + fileName);
        project.projectController.save();
    } */
}
