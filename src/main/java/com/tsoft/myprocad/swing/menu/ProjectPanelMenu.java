package com.tsoft.myprocad.swing.menu;

import javax.swing.*;

public class ProjectPanelMenu {
    // static class
    private ProjectPanelMenu() { }

    public static void createMenus() {
        updateMenuBar();
        updateToolBar();
    }

    public static void setVisible(boolean isVisible) {
        Menu.PROJECT_MENU.setVisible(isVisible);
        Menu.CALCULATION_MENU.setVisible(isVisible);
    }

    private static void updateMenuBar() {
        // Project menu
        JMenu projectMenu = Menu.PROJECT_MENU.addToMenuBar();
        projectMenu.add(Menu.EDIT_PROJECT.getMenuItem());
        projectMenu.add(Menu.EDIT_FOLDERS.getMenuItem());
        projectMenu.addSeparator();
        projectMenu.add(Menu.MATERIALS.getMenuItem());

        JMenu calculationMenu = Menu.CALCULATION_MENU.addToMenuBar();
        calculationMenu.add(Menu.CALCULATION_RIGHT_TRIANGLE.getMenuItem());
        calculationMenu.add(Menu.CALCULATION_TRIANGLE.getMenuItem());
    }

    private static void updateToolBar() {

    }
}
