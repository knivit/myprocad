package com.tsoft.myprocad.swing.menu;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.PlanPanel;
import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.math.Calculator;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class PlanPanelMenu {
    private static final transient Logger logger = Logger.getLogger(PlanPanel.class.getName());

    // static class
    private PlanPanelMenu() { }

    public static void createMenus() {
        updateMenuBar();
        updateToolBar();
    }

    public static void setVisible(boolean isVisible) {
        Menu.PLAN_MENU.setVisible(isVisible);
        Menu.SELECTION_MENU.setVisible(isVisible);
        Menu.REPORT_MENU.setVisible(isVisible);

        // tool bar
        Menu.UNDO.setVisible(isVisible);
        Menu.REDO.setVisible(isVisible);
        Menu.CUT.setVisible(isVisible);
        Menu.COPY.setVisible(isVisible);
        Menu.PASTE.setVisible(isVisible);
        Menu.SELECT.setVisible(isVisible);
        Menu.PAN.setVisible(isVisible);
        Menu.CREATE_WALLS.setVisible(isVisible);
        Menu.CREATE_BEAMS.setVisible(isVisible);
        Menu.CREATE_LABELS.setVisible(isVisible);
        Menu.CREATE_DIMENSION_LINES.setVisible(isVisible);
        Menu.CREATE_LABELS.setVisible(isVisible);
        Menu.CREATE_LEVEL_MARKS.setVisible(isVisible);
        Menu.ZOOM_IN.setVisible(isVisible);
        Menu.ZOOM_OUT.setVisible(isVisible);
    }

    private static void updateMenuBar() {
        // Plan menu
        JMenu planMenu = Menu.PLAN_MENU.addToMenuBar();
        planMenu.add(Menu.ADD_PLAN.getMenuItem());
        planMenu.add(Menu.DELETE_PLAN.getMenuItem());
        planMenu.addSeparator();
        planMenu.add(Menu.SHOW_PLAN_IN_3D.getMenuItem());
        planMenu.add(Menu.GENERATE_SCRIPT.getMenuItem());
        planMenu.add(Menu.COMMAND_WINDOW.getMenuItem());
        planMenu.addSeparator();
        planMenu.add(Menu.PLAN_PRINT_TO_PDF.getMenuItem());
        planMenu.add(Menu.PLAN_PRINT_PREVIEW.getMenuItem());
        planMenu.add(Menu.PLAN_PRINT.getMenuItem());
        planMenu.add(Menu.PLAN_EXPORT_TO_OBJ.getMenuItem());
        planMenu.addSeparator();
        planMenu.add(Menu.SELECT.getMenuItem());
        planMenu.add(Menu.PAN.getMenuItem());
        planMenu.add(Menu.CREATE_WALLS.getMenuItem());
        planMenu.add(Menu.CREATE_BEAMS.getMenuItem());
        planMenu.add(Menu.CREATE_DIMENSION_LINES.getMenuItem());
        planMenu.add(Menu.CREATE_LABELS.getMenuItem());
        planMenu.add(Menu.CREATE_LEVEL_MARKS.getMenuItem());
        planMenu.addSeparator();
        planMenu.add(Menu.UNDO.getMenuItem());
        planMenu.add(Menu.REDO.getMenuItem());
        planMenu.addSeparator();
        planMenu.add(Menu.ZOOM_IN.getMenuItem());
        planMenu.add(Menu.ZOOM_OUT.getMenuItem());
        planMenu.addSeparator();
        planMenu.add(Menu.FIND_MATERIALS_USAGE.getMenuItem());

        // Selection menu
        JMenu selectionMenu = Menu.SELECTION_MENU.addToMenuBar();
        selectionMenu.add(Menu.CUT.getMenuItem());
        selectionMenu.add(Menu.COPY.getMenuItem());
        selectionMenu.add(Menu.PASTE.getMenuItem());
        selectionMenu.add(Menu.DELETE.getMenuItem());
        selectionMenu.addSeparator();
        selectionMenu.add(Menu.ROTATE_CLOCKWISE.getMenuItem());
        selectionMenu.add(Menu.SPLIT_IN_TWO.getMenuItem());
        selectionMenu.addSeparator();
        selectionMenu.add(Menu.SELECT_ALL.getMenuItem());
        selectionMenu.add(Menu.SELECT_WALLS.getMenuItem());
        selectionMenu.add(Menu.SELECT_BY_MATERIAL.getMenuItem());
        selectionMenu.add(Menu.SELECT_BY_PATTERN.getMenuItem());

        // Report menu
        JMenu reportMenu = Menu.REPORT_MENU.addToMenuBar();
        reportMenu.add(Menu.MATERIALS_REPORT.getMenuItem());
    }

    private static void updateToolBar() {
        Menu.UNDO.addToToolBar();
        Menu.REDO.addToToolBar();
        Menu.addSeparatorToToolBar();

        Menu.CUT.addToToolBar();
        Menu.COPY.addToToolBar();
        Menu.PASTE.addToToolBar();
        Menu.addSeparatorToToolBar();

        Menu.SELECT.addToToolBar();
        Menu.PAN.addToToolBar();
        Menu.CREATE_WALLS.addToToolBar();
        Menu.CREATE_BEAMS.addToToolBar();
        Menu.CREATE_DIMENSION_LINES.addToToolBar();
        Menu.CREATE_LABELS.addToToolBar();
        Menu.CREATE_LEVEL_MARKS.addToToolBar();
        Menu.addSeparatorToToolBar();

        Menu.ZOOM_IN.addToToolBar();
        Menu.ZOOM_OUT.addToToolBar();
        Menu.addSeparatorToToolBar();

        JTextField calcExprField = new JTextField(20);
        JTextField calcResultField = new JTextField(8);
        calcExprField.addActionListener(e -> {
            Calculator calculator = new Calculator();
            Double result = calculator.calc(calcExprField.getText());
            String solution = calculator.getTextSolution();
            if (result == null) {
                if (solution == null) calcResultField.setText(L10.get(L10.CALCULATOR_ERROR));
                else calcResultField.setText(solution);
            } else calcResultField.setText(StringUtil.toString(result, 3));
        });

        Menu.addComponentToToolBar(new JLabel(L10.get(L10.CALCULATOR_LABEL)));
        Menu.CALCULATOR_HELP.addToToolBar();
        calcExprField.setMaximumSize(calcExprField.getPreferredSize());
        Menu.addComponentToToolBar(calcExprField);
        Menu.addComponentToToolBar(new JLabel(" = "));
        calcResultField.setMaximumSize(calcResultField.getPreferredSize());
        Menu.addComponentToToolBar(calcResultField);
    }

    public static void createPopupMenu(JComponent component) {
        JPopupMenu planViewPopup = new JPopupMenu();

        planViewPopup.add(Menu.SELECT_ALL.getPopupMenuItem());
        planViewPopup.add(Menu.CUT.getPopupMenuItem());
        planViewPopup.add(Menu.COPY.getPopupMenuItem());
        planViewPopup.add(Menu.PASTE.getPopupMenuItem());
        planViewPopup.add(Menu.DELETE.getPopupMenuItem());
        planViewPopup.addSeparator();

        planViewPopup.add(Menu.ROTATE_CLOCKWISE.getPopupMenuItem());
        planViewPopup.add(Menu.SPLIT_IN_TWO.getPopupMenuItem());
        planViewPopup.addSeparator();

        planViewPopup.add(Menu.SHOW_PLAN_IN_3D.getPopupMenuItem());
        planViewPopup.add(Menu.GENERATE_SCRIPT.getPopupMenuItem());

        component.setComponentPopupMenu(planViewPopup);
    }

    public static void installDefaultKeyboardActions(JComponent component) {
        class DefaultKeyAdapter extends KeyAdapter {
            Map<Integer, Menu> keys = new HashMap<>();
            Map<Integer, Menu> keysWithShift = new HashMap<>();
            Map<Integer, Menu> keysWithCtrl = new HashMap<>();

            public DefaultKeyAdapter() {
                keys.put(KeyEvent.VK_DELETE, Menu.DELETE);
                keys.put(KeyEvent.VK_BACK_SPACE, Menu.DELETE);
                keys.put(KeyEvent.VK_ESCAPE, Menu.ESCAPE);
                keys.put(KeyEvent.VK_LEFT, Menu.MOVE_SELECTION_LEFT);
                keys.put(KeyEvent.VK_RIGHT, Menu.MOVE_SELECTION_RIGHT);
                keys.put(KeyEvent.VK_DOWN, Menu.MOVE_SELECTION_DOWN);
                keys.put(KeyEvent.VK_UP, Menu.MOVE_SELECTION_UP);

                /* SHIFT + ... */
                keysWithShift.put(KeyEvent.VK_LEFT, Menu.MOVE_SELECTION_FAST_LEFT);
                keysWithShift.put(KeyEvent.VK_RIGHT, Menu.MOVE_SELECTION_FAST_RIGHT);
                keysWithShift.put(KeyEvent.VK_DOWN, Menu.MOVE_SELECTION_FAST_DOWN);
                keysWithShift.put(KeyEvent.VK_UP, Menu.MOVE_SELECTION_FAST_UP);

                /* CTRL + ... */
                keysWithCtrl.put(KeyEvent.VK_C, Menu.COPY);
                keysWithCtrl.put(KeyEvent.VK_X, Menu.CUT);
                keysWithCtrl.put(KeyEvent.VK_V, Menu.PASTE);
                keysWithCtrl.put(KeyEvent.VK_Z, Menu.UNDO);
                keysWithCtrl.put(KeyEvent.VK_A, Menu.SELECT_ALL);
                keysWithCtrl.put(KeyEvent.VK_EQUALS, Menu.ZOOM_IN);
                keysWithCtrl.put(KeyEvent.VK_MINUS, Menu.ZOOM_OUT);
                keysWithCtrl.put(KeyEvent.VK_ADD, Menu.ZOOM_IN);
                keysWithCtrl.put(KeyEvent.VK_SUBTRACT, Menu.ZOOM_OUT);
            }

            public void keyPressed(KeyEvent e) {
                String msg = "Key code=" + e.getKeyCode() +
                        ", Extended key code=" + e.getExtendedKeyCode() +
                        ", Shift=" + (e.isShiftDown() ? "ON" : "OFF") +
                        ", Control=" + (e.isControlDown() ? "ON" : "OFF") +
                        ", Alt=" + (e.isAltDown() ? "ON" : "OFF");
                logger.fine(msg);

                Map<Integer, Menu> keyMap = keys;
                if (e.isShiftDown()) keyMap = keysWithShift;
                if (e.isControlDown()) keyMap = keysWithCtrl;

                Menu menu = keyMap.get(e.getKeyCode());
                if (menu != null) menu.doAction();
            }
        }

        component.addKeyListener(new DefaultKeyAdapter());
    }
}
