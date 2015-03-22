package com.tsoft.myprocad.util;

import com.tsoft.myprocad.util.linealg.Geometry2DLib;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Geometry2DLibTest {
    public static final double DELTA = 0.000001;

    @Test
    public void getAngleBetweenTwoVectors() {
        /* |- */
        assertEquals(90, Math.toDegrees(Geometry2DLib.getAngleBetweenTwoSections(0, 0, 0, 1, 0, 0, 1, 0)), DELTA);

        /* |/ */
        assertEquals(45, Math.toDegrees(Geometry2DLib.getAngleBetweenTwoSections(0, 0, 0, 1, 0, 1, 1, 0)), DELTA);

        /* -| */
        assertEquals(90, Math.toDegrees(Geometry2DLib.getAngleBetweenTwoSections(0, 0, 1, 0, 1, 0, 1, 1)), DELTA);
    }
}
