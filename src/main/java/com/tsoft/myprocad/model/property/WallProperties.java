package com.tsoft.myprocad.model.property;

public class WallProperties {
    public static final ListenedField WALL_SHAPE = new ListenedField();

    public static final ListenedField X_START = new ListenedField();
    public static final ListenedField X_END = new ListenedField();

    public static final ListenedField Y_START = new ListenedField();
    public static final ListenedField Y_END = new ListenedField();

    public static final ListenedField Z_START = new ListenedField();
    public static final ListenedField Z_END = new ListenedField();

    public static final ListenedField PATTERN = new ListenedField();
    public static final ListenedField BORDER_WIDTH = new ListenedField();
    public static final ListenedField ALWAYS_SHOW_BORDERS = new ListenedField();

    public static final ListenedField MATERIAL = new ListenedField();
    public static final ListenedField SKIP_IN_REPORTS = new ListenedField();

    /** Special field for UNDO operation */
    public static final ListenedField UNDO_OPERATION = new ListenedField();
}
