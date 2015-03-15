package com.tsoft.myprocad.util;

import com.tsoft.myprocad.MyProCAD;

import java.awt.*;
import java.awt.dnd.DragSource;
import java.net.URL;

public class Cursors {
    public static final Cursor ROTATION;
    public static final Cursor ELEVATION;
    public static final Cursor HEIGHT;
    public static final Cursor POWER;
    public static final Cursor RESIZE;
    public static final Cursor MOVE;
    public static final Cursor PANNING;
    public static final Cursor DUPLICATION;
    public static final Cursor SELECTION;
    public static final Cursor DRAW;
    public static final Cursor DEFAULT;

    static {
        ROTATION = createCustomCursor("resources/cursors/rotation16x16.png", "resources/cursors/rotation32x32.png", "Rotation cursor", Cursor.MOVE_CURSOR);
        ELEVATION = createCustomCursor("resources/cursors/elevation16x16.png", "resources/cursors/elevation32x32.png", "Elevation cursor", Cursor.MOVE_CURSOR);
        HEIGHT = createCustomCursor("resources/cursors/height16x16.png", "resources/cursors/height32x32.png", "Height cursor", Cursor.MOVE_CURSOR);
        POWER = createCustomCursor("resources/cursors/power16x16.png", "resources/cursors/power32x32.png", "Power cursor", Cursor.MOVE_CURSOR);
        RESIZE = createCustomCursor("resources/cursors/resize16x16.png", "resources/cursors/resize32x32.png", "Resize cursor", Cursor.MOVE_CURSOR);
        MOVE = createCustomCursor("resources/cursors/move16x16.png", "resources/cursors/move32x32.png", "Move cursor", Cursor.MOVE_CURSOR);
        PANNING = createCustomCursor("resources/cursors/panning16x16.png", "resources/cursors/panning32x32.png", "Panning cursor", Cursor.HAND_CURSOR);
        DUPLICATION = DragSource.DefaultCopyDrop;
        SELECTION = Cursor.getDefaultCursor();
        DRAW = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        DEFAULT = Cursor.getDefaultCursor();
    }

    /**
     * Returns a custom cursor with a hot spot point at center of cursor.
     */
    private static Cursor createCustomCursor(String smallCursorImageResource, String largeCursorImageResource, String cursorName, int defaultCursor) {
        if (OperatingSystem.isMacOSX()) {
            smallCursorImageResource = smallCursorImageResource.replace(".png", "-macosx.png");
        }

        return createCustomCursor(MyProCAD.class.getResource(smallCursorImageResource),
                MyProCAD.class.getResource(largeCursorImageResource),
                0.5f, 0.5f, cursorName,
                Cursor.getPredefinedCursor(defaultCursor));
    }

    /**
     * Returns a custom cursor created from images in parameters.
     */
    private static Cursor createCustomCursor(URL smallCursorImageUrl, URL largeCursorImageUrl, float xCursorHotSpot, float yCursorHotSpot, String cursorName, Cursor defaultCursor) {
        return SwingTools.createCustomCursor(smallCursorImageUrl, largeCursorImageUrl,
                xCursorHotSpot, yCursorHotSpot, cursorName, defaultCursor);
    }
}
