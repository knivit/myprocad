package com.tsoft.myprocad.swing.menu;

import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.l10n.L10;

import com.tsoft.myprocad.viewcontroller.ApplicationController;

import java.util.UUID;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

public class Menu {
    public static JMenuBar menuBar;
    public static JToolBar toolBar;

    public static final Menu FILE_MENU = new Menu(L10.get(L10.FILE_MENU));

    public static final Menu NEW_PROJECT = new Menu(L10.get(L10.MENU_NEW_PROJECT_NAME));
    public static final Menu OPEN_PROJECT = new Menu(L10.get(L10.MENU_OPEN_PROJECT_NAME), "resources/icons/project-open.png");
    public static final Menu CLOSE_PROJECT = new Menu(L10.get(L10.MENU_CLOSE_PROJECT_NAME));
    public static final Menu SAVE_PROJECT = new Menu(L10.get(L10.MENU_SAVE_PROJECT_NAME), "resources/icons/project-save.png");
    public static final Menu SAVE_PROJECT_AS = new Menu(L10.get(L10.MENU_SAVE_PROJECT_AS_NAME));
    public static final Menu SETTINGS = new Menu(L10.get(L10.MENU_SETTINGS_NAME), "resources/icons/settings.png");
    public static final Menu HELP = new Menu(L10.get(L10.MENU_HELP_NAME), "resources/icons/help-browser.png");
    public static final Menu ABOUT = new Menu(L10.get(L10.MENU_ABOUT_NAME), "resources/icons/dialog-information.png");
    public static final Menu EXIT = new Menu(L10.get(L10.MENU_EXIT_NAME));

    public static final Menu PROJECT_MENU = new Menu(L10.get(L10.PROJECT_MENU));

    public static final Menu ADD_PLAN = new Menu(L10.get(L10.MENU_ADD_PLAN_NAME));
    public static final Menu DELETE_PLAN = new Menu(L10.get(L10.MENU_DELETE_PLAN_NAME));
    public static final Menu PLAN_PRINT_PREVIEW = new Menu(L10.get(L10.MENU_PRINT_PREVIEW_NAME), "resources/icons/document-print-preview.png");
    public static final Menu PLAN_PRINT = new Menu(L10.get(L10.MENU_PRINT_NAME), "resources/icons/document-print.png");
    public static final Menu PLAN_PRINT_TO_PDF = new Menu(L10.get(L10.MENU_PRINT_TO_PDF_NAME), "resources/icons/document-print-pdf.png");
    public static final Menu PLAN_EXPORT_TO_OBJ = new Menu(L10.get(L10.MENU_PLAN_EXPORT_TO_OBJ_NAME));
    public static final Menu MATERIALS = new Menu(L10.get(L10.MENU_MATERIALS_NAME));
    public static final Menu FIND_MATERIALS_USAGE = new Menu(L10.get(L10.MENU_FIND_MATERIALS_USAGE_NAME));
    public static final Menu PRINT_PREVIEW_SHOW_PREVIOUS_PAGE = new Menu(L10.get(L10.PRINT_PREVIEW_SHOW_PREVIOUS_PAGE), "resources/icons/go-previous.png");
    public static final Menu PRINT_PREVIEW_SHOW_NEXT_PAGE = new Menu(L10.get(L10.PRINT_PREVIEW_SHOW_NEXT_PAGE), "resources/icons/go-next.png");
    public static final Menu COMMAND_WINDOW = new Menu(L10.get(L10.COMMAND_WINDOW));

    public static final Menu CALCULATION_MENU = new Menu(L10.get(L10.CALCULATION_MENU));

    public static final Menu CALCULATION_TRIANGLE = new Menu(L10.get(L10.MENU_CALCULATION_TRIANGLE));
    public static final Menu CALCULATION_RIGHT_TRIANGLE = new Menu(L10.get(L10.MENU_CALCULATION_RIGHT_TRIANGLE));

    public static final Menu SELECTION_MENU = new Menu(L10.get(L10.SELECTION_MENU));

    public static final Menu UNDO = new Menu(L10.get(L10.MENU_UNDO_NAME), "resources/icons/edit-undo.png", L10.get(L10.MENU_UNDO_HINT));
    public static final Menu REDO = new Menu(L10.get(L10.MENU_REDO_NAME), "resources/icons/edit-redo.png", L10.get(L10.MENU_REDO_HINT));
    public static final Menu CUT = new Menu(L10.get(L10.MENU_CUT_NAME), "resources/icons/edit-cut.png", L10.get(L10.MENU_CUT_HINT));
    public static final Menu COPY = new Menu(L10.get(L10.MENU_COPY_NAME), "resources/icons/edit-copy.png", L10.get(L10.MENU_COPY_HINT));
    public static final Menu PASTE = new Menu(L10.get(L10.MENU_PASTE_NAME), "resources/icons/edit-paste.png", L10.get(L10.MENU_PASTE_HINT));
    public static final Menu DELETE = new Menu(L10.get(L10.MENU_DELETE_NAME), "resources/icons/edit-clear.png", L10.get(L10.MENU_DELETE_HINT));

    public static final Menu ROTATE_CLOCKWISE = new Menu(L10.get(L10.MENU_ROTATE_CLOCKWISE_NAME));
    public static final Menu SPLIT_IN_TWO = new Menu(L10.get(L10.MENU_SPLIT_IN_TWO_NAME));

    public static final Menu SELECT_BY_MATERIAL = new Menu(L10.get(L10.MENU_SELECT_BY_MATERIAL_NAME));
    public static final Menu SELECT_BY_PATTERN = new Menu(L10.get(L10.MENU_SELECT_BY_PATTERN_NAME));
    public static final Menu SELECT_ALL = new Menu(L10.get(L10.MENU_SELECT_ALL_NAME), "resources/icons/edit-select-all.png");
    public static final Menu SELECT_WALLS = new Menu(L10.get(L10.MENU_SELECT_WALLS_NAME));
    public static final Menu REMEMBER_SELECTION = new Menu(L10.get(L10.MENU_REMEMBER_SELECTION_NAME));
    public static final Menu RESTORE_LAST_SELECTION = new Menu(L10.get(L10.MENU_RESTORE_LAST_SELECTION_NAME));
    public static final Menu ADD_TO_LAST_SELECTION = new Menu(L10.get(L10.MENU_ADD_TO_LAST_SELECTION_NAME));
    public static final Menu CLEAR_REMEMBERED_SELECTIONS = new Menu(L10.get(L10.MENU_CLEAR_REMEMBERED_SELECTIONS_NAME));
    public static final Menu ESCAPE = new Menu(L10.get(L10.MENU_ESCAPE_NAME));
    public static final Menu MOVE_SELECTION_LEFT = new Menu(L10.get(L10.MENU_MOVE_SELECTION_LEFT_NAME));
    public static final Menu MOVE_SELECTION_UP = new Menu(L10.get(L10.MENU_MOVE_SELECTION_UP_NAME));
    public static final Menu MOVE_SELECTION_DOWN = new Menu(L10.get(L10.MENU_MOVE_SELECTION_DOWN_NAME));
    public static final Menu MOVE_SELECTION_RIGHT = new Menu(L10.get(L10.MENU_MOVE_SELECTION_RIGHT_NAME));
    public static final Menu MOVE_SELECTION_FAST_LEFT = new Menu(L10.get(L10.MENU_MOVE_SELECTION_FAST_LEFT_NAME));
    public static final Menu MOVE_SELECTION_FAST_UP = new Menu(L10.get(L10.MENU_MOVE_SELECTION_FAST_UP_NAME));
    public static final Menu MOVE_SELECTION_FAST_DOWN = new Menu(L10.get(L10.MENU_MOVE_SELECTION_FAST_DOWN_NAME));
    public static final Menu MOVE_SELECTION_FAST_RIGHT = new Menu(L10.get(L10.MENU_MOVE_SELECTION_FAST_RIGHT_NAME));
    public static final Menu GENERATE_SCRIPT = new Menu(L10.get(L10.MENU_GENERATE_SCRIPT_NAME));

    public static final Menu PLAN_MENU = new Menu(L10.get(L10.PLAN_MENU));

    public static final Menu SELECT = new Menu(L10.get(L10.MENU_SELECT_NAME), "resources/icons/plan-select.png", L10.get(L10.MENU_SELECT_HINT));
    public static final Menu PAN = new Menu(L10.get(L10.MENU_PAN_NAME), "resources/icons/plan-pan.png", L10.get(L10.MENU_PAN_HINT));
    public static final Menu CREATE_WALLS = new Menu(L10.get(L10.MENU_CREATE_WALLS_NAME), "resources/icons/plan-create-walls.png", L10.get(L10.MENU_CREATE_WALLS_HINT));
    public static final Menu CREATE_DIMENSION_LINES = new Menu(L10.get(L10.MENU_CREATE_DIMENSION_LINES_NAME), "resources/icons/plan-create-dimension-lines.png", L10.get(L10.MENU_CREATE_DIMENSION_LINES_HINT));
    public static final Menu CREATE_LABELS = new Menu(L10.get(L10.MENU_CREATE_LABELS_NAME), "resources/icons/plan-create-labels.png", L10.get(L10.MENU_CREATE_LABELS_HINT));
    public static final Menu CREATE_LEVEL_MARKS = new Menu(L10.get(L10.MENU_CREATE_LEVEL_MARKS_NAME), "resources/icons/plan-create-level-marks.png", L10.get(L10.MENU_CREATE_LEVEL_MARKS_HINT));
    public static final Menu EDIT_FOLDERS = new Menu(L10.get(L10.MENU_EDIT_FOLDERS_NAME));
    public static final Menu EDIT_PROJECT = new Menu(L10.get(L10.MENU_EDIT_PROJECT_NAME));

    public static final Menu ZOOM_IN = new Menu(L10.get(L10.MENU_ZOOM_IN_NAME), "resources/icons/plan-zoom-in.png", L10.get(L10.MENU_ZOOM_IN_HINT));
    public static final Menu ZOOM_OUT = new Menu(L10.get(L10.MENU_ZOOM_OUT_NAME), "resources/icons/plan-zoom-out.png", L10.get(L10.MENU_ZOOM_OUT_HINT));
    public static final Menu CALCULATOR_HELP = new Menu(L10.get(L10.MENU_CALCULATOR_HELP_NAME), "resources/icons/help-browser.png", L10.get(L10.MENU_CALCULATOR_HELP_HINT));

    public static final Menu NOTES_MENU = new Menu(L10.get(L10.NOTES_MENU));
    public static final Menu ADD_NOTES = new Menu(L10.get(L10.MENU_ADD_NOTES_NAME));
    public static final Menu DELETE_NOTES = new Menu(L10.get(L10.MENU_DELETE_NOTES_NAME));
    public static final Menu NOTES_PRINT_PREVIEW = new Menu(L10.get(L10.MENU_PRINT_PREVIEW_NAME), "resources/icons/document-print-preview.png");
    public static final Menu NOTES_PRINT = new Menu(L10.get(L10.MENU_PRINT_NAME), "resources/icons/document-print.png");
    public static final Menu NOTES_PRINT_TO_PDF = new Menu(L10.get(L10.MENU_PRINT_TO_PDF_NAME), "resources/icons/document-print-pdf.png");

    public static final Menu BEAM_MENU = new Menu(L10.get(L10.BEAM_MENU));
    public static final Menu ADD_BEAM = new Menu(L10.get(L10.MENU_ADD_BEAM_NAME));
    public static final Menu DELETE_BEAM = new Menu(L10.get(L10.MENU_DELETE_BEAM_NAME));
    public static final Menu BEAM_PRINT_PREVIEW = new Menu(L10.get(L10.MENU_PRINT_PREVIEW_NAME), "resources/icons/document-print-preview.png");
    public static final Menu BEAM_PRINT = new Menu(L10.get(L10.MENU_PRINT_NAME), "resources/icons/document-print.png");
    public static final Menu BEAM_PRINT_TO_PDF = new Menu(L10.get(L10.MENU_PRINT_TO_PDF_NAME), "resources/icons/document-print-pdf.png");

    public static final Menu WOOD_BEAM_MENU = new Menu(L10.get(L10.WOOD_BEAM_MENU));
    public static final Menu ADD_WOOD_BEAM = new Menu(L10.get(L10.MENU_ADD_WOOD_BEAM_NAME));
    public static final Menu DELETE_WOOD_BEAM = new Menu(L10.get(L10.MENU_DELETE_WOOD_BEAM_NAME));
    public static final Menu WOOD_BEAM_PRINT_PREVIEW = new Menu(L10.get(L10.MENU_PRINT_PREVIEW_NAME), "resources/icons/document-print-preview.png");
    public static final Menu WOOD_BEAM_PRINT = new Menu(L10.get(L10.MENU_PRINT_NAME), "resources/icons/document-print.png");
    public static final Menu WOOD_BEAM_PRINT_TO_PDF = new Menu(L10.get(L10.MENU_PRINT_TO_PDF_NAME), "resources/icons/document-print-pdf.png");

    public static final Menu REPORT_MENU = new Menu(L10.get(L10.REPORT_MENU));

    public static final Menu MATERIALS_REPORT = new Menu(L10.get(L10.MENU_MATERIALS_REPORT_NAME));

    private String id;
    private String name;
    private String hint;
    private ImageIcon icon;
    private JMenuItem menuItem;
    private JButton toolBarButton;

    private Menu(String name) {
        this(name, null);
    }

    private Menu(String name, String iconFileName) {
        this(name, iconFileName, null);
    }

    private Menu(String name, String iconFileName, String hint) {
        id = UUID.randomUUID().toString();

        this.name = name;
        this.hint = hint;

        // we need an exception to be thrown here if the resource doesn't exists
        icon = iconFileName == null ? null : new ImageIcon(MyProCAD.class.getResource(iconFileName));
    }

    public static void init() {
        menuBar = new JMenuBar();
        toolBar = new JToolBar();
    }

    public String getName() {
        return name;
    }

    private ImageIcon getIcon() {
        return icon;
    }

    public void doAction() { ApplicationController.getInstance().doMenuAction(this); }

    public JMenu addToMenuBar() {
        menuItem = new JMenu(name);
        menuBar.add(menuItem);
        return (JMenu)menuItem;
    }

    public void addToToolBar() {
        toolBarButton = new JButton();
        toolBarButton.setToolTipText(hint);
        if (icon != null) toolBarButton.setIcon(getIcon());
        toolBarButton.addActionListener((e) -> ApplicationController.getInstance().doMenuAction(this));
        toolBar.add(toolBarButton);
    }

    private JMenuItem createMenuItem() {
        JMenuItem menuItem = (icon == null) ? new JMenuItem(name) : new JMenuItem(name, getIcon());
        menuItem.addActionListener((e) -> ApplicationController.getInstance().doMenuAction(this));
        return menuItem;
    }

    public JMenuItem getMenuItem() {
        if (menuItem == null) menuItem = createMenuItem();
        return menuItem;
    }

    public JMenuItem getPopupMenuItem() {
        return createMenuItem();
    }

    public void setVisible(boolean visible) {
        getMenuItem().setVisible(visible);
        if (toolBarButton != null) toolBarButton.setEnabled(visible);
    }

    public static void addSeparatorToToolBar() {
        toolBar.addSeparator();
    }

    public static void addComponentToToolBar(JComponent component) {
        toolBar.add(component);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Menu menu = (Menu) o;

        if (!id.equals(menu.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
