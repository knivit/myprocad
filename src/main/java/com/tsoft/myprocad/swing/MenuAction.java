package com.tsoft.myprocad.swing;

import com.tsoft.myprocad.swing.menu.Menu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuAction extends AbstractAction {
    private Menu menu;
    private ActionListener actionListener;

    public MenuAction(Menu menu, ActionListener actionListener) {
        this.menu = menu;
        this.actionListener = actionListener;

        putValue(Action.NAME, menu.getName());
    }

    public Menu getMenu() {
        return menu;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        actionListener.actionPerformed(e);
    }
}