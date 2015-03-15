package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.model.Project;
import com.tsoft.myprocad.model.calculation.Beam;
import com.tsoft.myprocad.swing.*;
import com.tsoft.myprocad.swing.menu.BeamPanelMenu;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;
import com.tsoft.myprocad.lib.mm.MMLib;
import com.tsoft.myprocad.viewcontroller.property.calculation.BeamPropertiesController;

import javax.swing.*;
import java.util.Arrays;
import java.util.concurrent.Callable;

public class BeamController implements ProjectItemController {
    private ProjectController projectController;
    private Project project;

    private Beam beam;
    private BeamPanel beamPanel;
    private boolean isInitialized;

    public static BeamController createBeamController(Beam beam) {
        BeamController beamController = new BeamController(beam);
        beamController.beamPanel = new BeamPanel();

        return beamController;
    }

    private BeamController(Beam beam) {
        this.beam = beam;
    }

    @Override
    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
        this.project = projectController.project;
    }

    @Override
    public void afterOpen() {
        if (isInitialized) return;
        isInitialized = true;

        PropertiesManagerPanel propertiesManagerPanel = new PropertiesManagerPanel();
        beamPanel.afterOpen(propertiesManagerPanel);

        BeamPropertiesController beamPropertiesController = new BeamPropertiesController(propertiesManagerPanel);
        beamPropertiesController.selectObjects(Arrays.asList(beam));

        updatePanel();
    }

    private void updatePanel() {
        String result = MMLib.calcStatic(beam);

        // show results
        for (int i = 0; i < beam.solutions.length; i ++) {
            beamPanel.setImage(i, beam.solutions[i].getImage());
        }
        beamPanel.setText(result);
    }

    public void beamChanged() {
        updatePanel();
        project.setModified(true);
    }

    @Override
    public void beforeClose() {
        beforeDeactivation();
    }

    @Override
    public void beforeDeactivation() {
        BeamPanelMenu.setVisible(false);
    }

    @Override
    public void afterActivation() {
        BeamPanelMenu.setVisible(true);
    }

    @Override
    public JComponent getParentComponent() {
        return beamPanel.getParentComponent();
    }

    private void addBeam() { projectController.addProjectItem(); }

    private void deleteBeam() { projectController.deleteProjectItem(beam); }

    private void printBeamToPDF() { throw new IllegalStateException("Not implemented"); }

    private void printPreviewBeam() { throw new IllegalStateException("Not implemented"); }

    public Callable<Void> showPrintDialog() { throw new IllegalStateException("Not implemented"); }

    @Override
    public boolean doMenuAction(Menu menu) {
        if (Menu.ADD_BEAM.equals(menu)) { addBeam(); return true; }
        if (Menu.DELETE_BEAM.equals(menu)) { deleteBeam(); return true; }

        if (Menu.BEAM_PRINT_TO_PDF.equals(menu)) { printBeamToPDF(); return true; }
        if (Menu.BEAM_PRINT_PREVIEW.equals(menu)) { printPreviewBeam(); return true; }
        if (Menu.BEAM_PRINT.equals(menu)) { showPrintDialog(); return true; }
        return false;
    }

    @Override
    public void materialListChanged() {
        //
    }
}
