package com.tsoft.myprocad;

import javax.swing.UIManager;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Application;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.util.OperatingSystem;
import com.tsoft.myprocad.viewcontroller.ApplicationController;

/**
 * Used notification system:
 *   1) Model notifies its controller (if exists)
 *   2) Model notifies parent model (if exists)
 *   3) Controller notifies its view
 *
 * For example,
 *   1) Wall notifies Plan
 *      Wall doesn't have a controller
 *   2) Plan notifies PlanController and Project
 *      PlanController notifies PlanPanel
 *   3) Project notifies ProjectController
 *      ProjectController notifies ProjectPanel
 *   etc
 */

public class MyProCAD {
    public static final String APPLICATION_NAME = "MyProCAD";
    public static final String APPLICATION_VERSION = "1.0";

    public static void main(final String [] args) {
        initLookAndFeel();

        MyProCAD myProCAD = new MyProCAD();
        myProCAD.init();
        myProCAD.start();
    }

    public MyProCAD() {
        ApplicationController.clearInstance();
        Application.clearInstance();
    }

    public void init() {
        ApplicationController applicationController = ApplicationController.getInstance();
        Menu.init();
        applicationController.init();
    }

    public String getName() {
        return L10.get(L10.APPLICATION_NAME);
    }

    private void start() {
        ApplicationController.getInstance().show();
    }

    // Init look and feel afterwards to ensure that Swing takes into account default locale change
    private static void initLookAndFeel() {
        try {
            // Apply current system look and feel if swing.defaultlaf isn't defined
            UIManager.setLookAndFeel(System.getProperty("swing.defaultlaf", UIManager.getSystemLookAndFeelClassName()));

            // Change default titled borders under Mac OS X 10.5
            if (OperatingSystem.isMacOSXLeopardOrSuperior()) {
                UIManager.put("TitledBorder.border", UIManager.getBorder("TitledBorder.aquaVariant"));
            }
            SwingTools.updateSwingResourceLanguage();
        } catch (Exception ex) {
            // Too bad keep current look and feel
        }
    }
}
