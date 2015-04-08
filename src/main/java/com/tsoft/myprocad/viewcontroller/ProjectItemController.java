package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.swing.menu.Menu;

import javax.swing.*;

public interface ProjectItemController {
    public abstract void setProjectController(ProjectController projectController);

    public abstract void afterOpen();

    public abstract void afterActivation();

    public abstract void beforeClose();

    public abstract void beforeDeactivation();

    public abstract JComponent getParentComponent();

    public boolean doMenuAction(Menu menu, Menu.Source source);

    public abstract void materialListChanged();
}
