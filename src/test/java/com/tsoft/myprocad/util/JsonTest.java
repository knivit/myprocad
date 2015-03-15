package com.tsoft.myprocad.util;

import com.tsoft.myprocad.AbstractItemTest;
import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.viewcontroller.ApplicationController;
import com.tsoft.myprocad.viewcontroller.PasteOperation;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.ProjectController;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonTest {
    @Test
    public void itemsTest() throws IOException {
        MyProCAD myProCAD = new MyProCAD();
        myProCAD.init();

        Menu.NEW_PROJECT.doAction();

        ProjectController projectController = ApplicationController.getInstance().getActiveProjectController();
        for (ProjectItemType type : ProjectItemType.values()) {
            projectController.addItem(type, UUID.randomUUID().toString(), 0);
        }

        Project project = projectController.project;
        Folder folder = project.getActiveFolder();
        Optional<ProjectItem> planItem = folder.getItems().stream().filter(ProjectItem::isPlan).findFirst();
        assertNotNull(planItem.get());

        Plan plan = (Plan)planItem.get();
        Wall wall = plan.createWall(0, 1000, 0, 1000, 0, 100);
        wall.setPattern(Pattern.BRICK);
        Label label = plan.createLabel(2000, 0, "Test");
        label.setRotation(Rotation.ANGLE_180);
        DimensionLine dimensionLine = plan.createDimensionLine(0, 1000, 0, 0, -100);
        dimensionLine.setEndPointShapeType(PointShapeType.NONE);
        LevelMark levelMark = plan.createLevelMark(0, 0, 0, 0);
        levelMark.setRotation(Rotation.ANGLE_90);

        // save it
        project.setFileName(File.createTempFile("TestProject", null).getAbsolutePath());
        Menu.SAVE_PROJECT.doAction();

        // serialize
        String result = serialize(project);
        System.out.println(result);

        // deserialize
        ByteArrayInputStream stream = new ByteArrayInputStream(result.getBytes("UTF-8"));
        JsonReader reader = new JsonReader(stream);
        project = new Project();
        project.fromJson(reader);

        // serialize again
        String result2 = serialize(project);

        // compare both serialized versions
        assertEquals(result, result2);
    }

    @Test
    public void clipboardTest() {
        MyProCAD myProCAD = new MyProCAD();
        myProCAD.init();

        Menu.NEW_PROJECT.doAction();

        ProjectController projectController = ApplicationController.getInstance().getActiveProjectController();
        Project project = projectController.project;

        // create items on the default plan
        Plan plan1 = project.getAllPlans().get(0);
        Wall wall1 = plan1.createWall(0, 1000, 0, 1000, 0, 1000);
        DimensionLine dimensionLine1 = plan1.createDimensionLine(0, 1000, 0, 0, -100);
        Label label1 = plan1.createLabel(0, 0, "Test");
        LevelMark levelMark1 = plan1.createLevelMark(0, 1000, 0, 1000);

        // select and copy them to the clipboard
        Menu.SELECT_ALL.doAction();
        Menu.COPY.doAction();

        // create another plan and paste items on it
        Plan plan2 = (Plan)projectController.addItem(ProjectItemType.PLAN, UUID.randomUUID().toString(), 1);
        projectController.projectPanel.selectTab(1);
        PlanController planController2 = (PlanController)projectController.getActiveController();
        assertEquals(plan2, planController2.getPlan());
        plan2.setPasteOperation(PasteOperation.MOVE_TO);
        Menu.PASTE.doAction();

        // here is the checks
        // we can't use item's equals() as it compares by id and id is changes during copy/paste
        assertEquals(1, plan2.getWalls().size());
        Wall wall2 = plan2.getWalls().get(0);
        AbstractItemTest.assertWallEquals(wall1, wall2);
    }

    private String serialize(Project project) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        JsonWriter writer = new JsonWriter(stream);
        writer.toJson(project);

        return new String(stream.toByteArray(), "UTF-8");
    }
}
