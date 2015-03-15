package com.tsoft.myprocad.swing.menu;

import javax.swing.*;

public class WoodBeamPanelMenu {
    // static class
    private WoodBeamPanelMenu() { }

    public static void createMenus() {
        updateMenuBar();
        updateToolBar();
    }

    public static void setVisible(boolean isVisible) {
        Menu.WOOD_BEAM_MENU.setVisible(isVisible);
    }

    private static void updateMenuBar() {
        // Beam menu
        JMenu menu = Menu.WOOD_BEAM_MENU.addToMenuBar();
        menu.add(Menu.ADD_WOOD_BEAM.getMenuItem());
        menu.add(Menu.DELETE_WOOD_BEAM.getMenuItem());
        menu.addSeparator();
        menu.add(Menu.WOOD_BEAM_PRINT_TO_PDF.getMenuItem());
        menu.add(Menu.WOOD_BEAM_PRINT_PREVIEW.getMenuItem());
        menu.add(Menu.WOOD_BEAM_PRINT.getMenuItem());
    }

    private static void updateToolBar() { }
}
