package com.tsoft.myprocad.swing.menu;

import javax.swing.*;

public class NotesPanelMenu {
    // static class
    private NotesPanelMenu() { }

    public static void createMenus() {
        updateMenuBar();
        updateToolBar();
    }

    public static void setVisible(boolean isVisible) {
        Menu.NOTES_MENU.setVisible(isVisible);
    }

    private static void updateMenuBar() {
        // Notes menu
        JMenu notesMenu = Menu.NOTES_MENU.addToMenuBar();
        notesMenu.add(Menu.ADD_NOTES.getMenuItem());
        notesMenu.add(Menu.DELETE_NOTES.getMenuItem());
        notesMenu.addSeparator();
        notesMenu.add(Menu.NOTES_PRINT_TO_PDF.getMenuItem());
        notesMenu.add(Menu.NOTES_PRINT_PREVIEW.getMenuItem());
        notesMenu.add(Menu.NOTES_PRINT.getMenuItem());
    }

    private static void updateToolBar() {

    }
}
