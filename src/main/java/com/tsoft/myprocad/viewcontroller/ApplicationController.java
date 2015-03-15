package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.l10n.Language;
import com.tsoft.myprocad.model.Application;
import com.tsoft.myprocad.model.property.ListenedField;
import com.tsoft.myprocad.model.property.ProjectProperties;
import com.tsoft.myprocad.swing.dialog.DialogButton;
import com.tsoft.myprocad.swing.dialog.PropertiesDialogPanel;
import com.tsoft.myprocad.swing.menu.ApplicationPanelMenu;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;
import com.tsoft.myprocad.util.ContentManager;
import com.tsoft.myprocad.util.FileRecorder;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.*;
import com.tsoft.myprocad.model.Project;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.viewcontroller.property.AbstractPropertiesController;
import com.tsoft.myprocad.viewcontroller.property.ApplicationPropertiesController;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class ApplicationController {
    private ApplicationPanel applicationPanel;
    private List<ProjectController> projectControllers = new ArrayList<>();

    private static ApplicationController applicationController;

    public static ApplicationController getInstance() {
        if (applicationController != null) return applicationController;

        applicationController = new ApplicationController();
        applicationController.createApplication();

        return applicationController;
    }

    public static void clearInstance() { applicationController = null; }

    private ApplicationController() {
    }

    private void createApplication() {
        Application application = Application.getInstance();
        if ("ru".equals(System.getProperty("user.language"))) application.setLanguage(Language.RUSSIAN);
        else application.setLanguage(Language.ENGLISH);

        String preferencesFileName;
        String homeFolder = System.getProperty("user.home");
        try {
            Path preferencesFolder = Files.createDirectories(Paths.get(homeFolder + "/." + MyProCAD.class.getSimpleName()));
            preferencesFileName = preferencesFolder.toString() + "/App.properties";
            if (new File(preferencesFileName).canRead()) {
                try (FileInputStream in = new FileInputStream(preferencesFileName)) {
                    JsonReader reader = new JsonReader(in);
                    application.fromJson(reader);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();

            // working without preferences
            preferencesFileName = null;
        }

        application.setPreferencesFileName(preferencesFileName);
    }

    public void init() {
        ApplicationPanelMenu.createMenus();
        applicationPanel = new ApplicationPanel(this);
    }

    public String getVersion() {
        return MyProCAD.APPLICATION_VERSION;
    }

    public ProjectController getActiveProjectController() {
        int index = applicationPanel.getSelectedProjectTab();
        return index == -1 ? null : projectControllers.get(index);
    }

    private ProjectController getProjectControllerByProject(Project project) {
        Optional<ProjectController> projectController = projectControllers.stream().filter(e -> e.project.equals(project)).findFirst();
        return projectController.isPresent() ? projectController.get() : null;
    }

    public void projectChanged(Project project, ListenedField property) {
        // ProjectProperties.FILE_NAME is used both for file name changing and project's changes
        if (ProjectProperties.FILE_NAME.equals(property)) {
            int index = projectControllers.indexOf(getProjectControllerByProject(project));
            if (index != -1) applicationPanel.projectChanged(index, project);
        }
    }

    public void show() {
        applicationPanel.displayView();
        applicationPanel.bringToFront();
    }

    private void create() {
        openProject(new Project(), L10.get(L10.PROJECT_CREATE_MESSAGE));
    }

    private void open() {
        String fileName = SwingTools.showOpenDialog(ContentManager.ContentType.MY_PRO_CAD);
        if (fileName != null) open(fileName);
    }

    private void open(final String fileName) {
        // Check if requested project isn't already opened
        boolean alreadyOpen = projectControllers.stream().anyMatch(e -> fileName.equalsIgnoreCase(e.project.getFileName()));
        if (alreadyOpen) {
            SwingTools.showMessage(L10.get(L10.PROJECT_ALREADY_OPEN, fileName));
            return;
        }

        FileRecorder fileRecorder = new FileRecorder();
        Project project = fileRecorder.readProject(fileName);
        if (project == null) {
            SwingTools.showError(L10.get(L10.CANT_OPEN_PROJECT, fileName));
        } else {
            project.setFileName(fileName);
            openProject(project, L10.get(L10.PROJECT_OPEN_MESSAGE));
        }
    }

    private void openProject(Project project, String caption) {
    //    ThreadedTaskController task = new ThreadedTaskController(caption);

    //    task.execute(() -> {
            ProjectController projectController = ProjectController.createProjectController(project);
            projectControllers.add(projectController);

            applicationPanel.addProject(project.getName(), projectController.projectPanel);

            projectController.afterOpen();
    //        return null;
    //    });
    }

    private void save() {
        ProjectController projectController = getActiveProjectController();
        if (projectController != null) projectController.save();
    }

    private void saveAs() {
        ProjectController projectController = getActiveProjectController();
        if (projectController != null) projectController.saveAs();
    }

    public boolean canCloseApplication() {
        if (projectControllers.isEmpty()) return true;

        close();
        return false;
    }

    public void close() {
        ProjectController projectController = getActiveProjectController();
        if (projectController == null) return;

        SwingTools.SaveAnswer answer = SwingTools.SaveAnswer.DO_NOT_SAVE;
        if (projectController.project.isModified()) {
            answer = applicationPanel.confirmSave(projectController.project.getTitle());
        }

        switch (answer) {
            case SAVE: {
                projectController.beforeClose();
                save();
                closeProject(projectController);
                break;
            }
            case DO_NOT_SAVE: {
                projectController.beforeClose();
                closeProject(projectController);
                break;
            }
            default: {
                break;
            }
        }
    }

    private void closeProject(ProjectController projectController) {
        int index = projectControllers.indexOf(projectController);
        projectControllers.remove(projectController);
        applicationPanel.afterProjectClose(index, projectControllers.isEmpty());

        ProjectController activeController = getActiveProjectController();
        if (activeController != null) activeController.afterActivation();
    }

    public void exit() {
        if (applicationPanel.confirmExit()) System.exit(0);
    }

    private void help() {
        String helpPageUrl = "http://myprocad.com";
        try {
            Desktop.getDesktop().browse(new URI(helpPageUrl));
        } catch (Exception e) {
            e.printStackTrace();
            SwingTools.showMessage(L10.get(L10.CANT_OPEN_HELP_PAGE, helpPageUrl));
        }
    }

    private void about() {
        applicationPanel.showAboutDialog();
    }

    private void settings() {
        Function<PropertiesManagerPanel, AbstractPropertiesController> constructor = ApplicationPropertiesController::new;
        PropertiesDialogPanel settingsDialog = new PropertiesDialogPanel(constructor, Application.getInstance());
        settingsDialog.displayView(L10.get(L10.SETTINGS_DIALOG_TITLE), DialogButton.CLOSE);
    }

    public void modeChanged(String text) {
        if (applicationPanel != null)
            applicationPanel.setModeStatusLabel(text);
    }

    public void setStatusPanelVisible(boolean isVisible) {
        if (applicationPanel != null)
            applicationPanel.setStatusPanelVisible(isVisible);
    }

    public void setXStatusLabel(String text) {
        applicationPanel.setXStatusLabel(text);
    }

    public void setYStatusLabel(String text) {
        applicationPanel.setYStatusLabel(text);
    }

    public void calculatorHelp() {
        JOptionPane.showMessageDialog(null, L10.get(L10.CALCULATOR_HELP_TEXT));
    }

    public boolean doMenuAction(Menu menu) {
        /* Application */
        if (Menu.NEW_PROJECT.equals(menu)) { create(); return true; }
        if (Menu.OPEN_PROJECT.equals(menu)) { open(); return true; }
        if (Menu.SAVE_PROJECT.equals(menu)) { save(); return true; }
        if (Menu.SAVE_PROJECT_AS.equals(menu)) { saveAs(); return true; }
        if (Menu.CLOSE_PROJECT.equals(menu)) { close(); return true; }
        if (Menu.SETTINGS.equals(menu)) { settings(); return true; }
        if (Menu.EXIT.equals(menu)) { exit(); return true; }
        if (Menu.HELP.equals(menu)) { help(); return true; }
        if (Menu.ABOUT.equals(menu)) { about(); return true; }

        if (Menu.CALCULATOR_HELP.equals(menu)) { calculatorHelp(); return true; }

        /* Project */
        ProjectController projectController = getActiveProjectController();
        if (projectController == null) return false;
        if (projectController.doMenuAction(menu)) return true;
        throw new IllegalArgumentException("Unknown menu " + menu.getName());
    }
}
