package com.tsoft.myprocad;

import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.viewcontroller.ApplicationController;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.ProjectController;
import org.junit.Before;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

public abstract class AbstractItemTest {
    protected Plan plan;
    protected PlanController planController;

    @Before
    public void setUp() throws InvocationTargetException, InterruptedException {
        MyProCAD myProCAD = new MyProCAD();
        myProCAD.init();

        Menu.NEW_PROJECT.doAction();

        ProjectController projectController = ApplicationController.getInstance().getActiveProjectController();
        Project project = projectController.project;
        Folder folder = project.getActiveFolder();

        Optional<ProjectItem> planItem = folder.getItems().stream().filter(ProjectItem::isPlan).findFirst();
        plan = (Plan)planItem.get();

        planController = plan.getController();
        planController.setProjectController(projectController);
        planController.afterOpen();
    }

    protected static void assertItemEquals(Item item1, Item item2) {
        assertEquals(item1.getTypeName(), item2.getTypeName());
        assertEquals(item1.getXStart(), item2.getXStart());
        assertEquals(item1.getXEnd(), item2.getXEnd());
        assertEquals(item1.getYStart(), item2.getYStart());
        assertEquals(item1.getYEnd(), item2.getYEnd());
        assertEquals(item1.getZStart(), item2.getZStart());
        assertEquals(item1.getZEnd(), item2.getZEnd());
    }

    public static void assertWallEquals(Wall wall1, Wall wall2) {
        assertItemEquals(wall1, wall2);
        assertEquals(wall1.getPattern(), wall2.getPattern());
        assertEquals(wall1.getMaterial(), wall2.getMaterial());
    }
}
