package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.lib.mm.WoodBeamLib;
import com.tsoft.myprocad.model.Project;
import com.tsoft.myprocad.model.calculation.WoodBeam;
import com.tsoft.myprocad.swing.WoodBeamPanel;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.swing.menu.WoodBeamPanelMenu;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;
import com.tsoft.myprocad.viewcontroller.property.calculation.WoodBeamPropertiesController;

import javax.swing.*;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class WoodBeamController implements ProjectItemController {
    private ProjectController projectController;
    private Project project;
    private boolean isInitialized;

    private WoodBeam woodBeam;
    private WoodBeamPanel woodBeamPanel;
    private WoodBeamPropertiesController woodBeamPropertiesController;

    public static WoodBeamController createWoodBeamController(WoodBeam woodBeam) {
        WoodBeamController woodBeamController = new WoodBeamController(woodBeam);
        woodBeamController.woodBeamPanel = new WoodBeamPanel();

        return woodBeamController;
    }

    private WoodBeamController(WoodBeam woodBeam) {
        this.woodBeam = woodBeam;
    }

    @Override
    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
        this.project = projectController.project;
    }

    private void updatePanel() {
        String result = WoodBeamLib.calcStatic(woodBeam);
        woodBeamPanel.setText(result);
    }

    public void woodBeamChanged() {
        updatePanel();
        project.setModified(true);
    }

    @Override
    public void afterOpen() {
        if (isInitialized) return;
        isInitialized = true;

        PropertiesManagerPanel propertiesManagerPanel = new PropertiesManagerPanel();
        woodBeamPanel.afterOpen(propertiesManagerPanel);

        woodBeamPropertiesController = new WoodBeamPropertiesController(propertiesManagerPanel);
        woodBeamPropertiesController.selectObjects(Arrays.asList(woodBeam));

        updatePanel();
    }

    @Override
    public void beforeClose() {
        beforeDeactivation();
    }

    @Override
    public void beforeDeactivation() {
        WoodBeamPanelMenu.setVisible(false);
    }

    @Override
    public void afterActivation() {
        WoodBeamPanelMenu.setVisible(true);
    }

    @Override
    public JComponent getParentComponent() {
        return woodBeamPanel.getParentComponent();
    }

    private void addWoodBeam() { projectController.addProjectItem(); }

    private void deleteWoodBeam() { projectController.deleteProjectItem(woodBeam); }

    private void printWoodBeamToPDF() { throw new IllegalStateException("Not implemented"); }

    private void printPreviewWoodBeam() { throw new IllegalStateException("Not implemented"); }

    public Callable<Void> showPrintDialog() { throw new IllegalStateException("Not implemented"); }

    @Override
    public boolean doMenuAction(Menu menu) {
        if (Menu.ADD_WOOD_BEAM.equals(menu)) { addWoodBeam(); return true; }
        if (Menu.DELETE_WOOD_BEAM.equals(menu)) { deleteWoodBeam(); return true; }

        if (Menu.WOOD_BEAM_PRINT_TO_PDF.equals(menu)) { printWoodBeamToPDF(); return true; }
        if (Menu.WOOD_BEAM_PRINT_PREVIEW.equals(menu)) { printPreviewWoodBeam(); return true; }
        if (Menu.WOOD_BEAM_PRINT.equals(menu)) { showPrintDialog(); return true; }
        return false;
    }

    @Override
    public void materialListChanged() {
        //
    }
}
