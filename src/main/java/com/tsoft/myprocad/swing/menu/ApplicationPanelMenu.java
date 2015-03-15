package com.tsoft.myprocad.swing.menu;

import javax.swing.*;

public class ApplicationPanelMenu {
    // static class
    private ApplicationPanelMenu() { }

    public static void createMenus() {
        updateMenuBar();
        updateToolBar();

        // create the sub-menus
        ProjectPanelMenu.createMenus();
        PlanPanelMenu.createMenus();
        NotesPanelMenu.createMenus();

        // hide them, they will be activated on project open
        NotesPanelMenu.setVisible(false);
        PlanPanelMenu.setVisible(false);
        ProjectPanelMenu.setVisible(false);
    }

    private static void updateMenuBar() {
        // File menu
        JMenu fileMenu = Menu.FILE_MENU.addToMenuBar();
        fileMenu.add(Menu.NEW_PROJECT.getMenuItem());
        fileMenu.add(Menu.OPEN_PROJECT.getMenuItem());
        fileMenu.add(Menu.SAVE_PROJECT.getMenuItem());
        fileMenu.add(Menu.SAVE_PROJECT_AS.getMenuItem());
        fileMenu.add(Menu.CLOSE_PROJECT.getMenuItem());
        fileMenu.addSeparator();
        fileMenu.add(Menu.SETTINGS.getMenuItem());
        fileMenu.addSeparator();
        fileMenu.add(Menu.HELP.getMenuItem());
        fileMenu.add(Menu.ABOUT.getMenuItem());
        fileMenu.addSeparator();
        fileMenu.add(Menu.EXIT.getMenuItem());
    }

    private static void updateToolBar() {
        Menu.OPEN_PROJECT.addToToolBar();
        Menu.SAVE_PROJECT.addToToolBar();
        Menu.addSeparatorToToolBar();
    }
}
