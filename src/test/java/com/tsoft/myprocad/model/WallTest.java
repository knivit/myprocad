package com.tsoft.myprocad.model;

import com.tsoft.myprocad.AbstractItemTest;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class WallTest extends AbstractItemTest {
    @Test
    public void testRotate() {
        Wall wall = plan.createWall(1, 3, 1, 4, 0, 1);
        planController.selectItem(wall);

        wall.rotate(1, 1, -90);

        assertEquals(1, wall.getXStart());
        assertEquals(-1, wall.getYStart());
        assertEquals(4, wall.getXEnd());
        assertEquals(1, wall.getYEnd());
    }

    @Test
    public void testShapes() {
        Wall wall;

        wall = plan.createWall(0, 1000, 0, 1000, 0, 1);
        wall.setDiagonalWidth(200);
        assertEquals(Math.round(200/Math.sin(Math.toRadians(45))), wall.getDiagonalOffset("11"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(45))), wall.getDiagonalOffset("12"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(45))), wall.getDiagonalOffset("21"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(45))), wall.getDiagonalOffset("22"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(45))), wall.getDiagonalOffset("31"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(45))), wall.getDiagonalOffset("32"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(45))), wall.getDiagonalOffset("41"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(45))), wall.getDiagonalOffset("42"));

        wall = plan.createWall(0, (float)Math.sqrt(3)*1000, 0, 1000, 0, 1);
        wall.setDiagonalWidth(200);
        assertEquals(Math.round(200/Math.sin(Math.toRadians(60))), wall.getDiagonalOffset("32"));
        assertEquals(Math.round(200 / Math.sin(Math.toRadians(60))), wall.getDiagonalOffset("22"));
        assertEquals(Math.round(200 / Math.sin(Math.toRadians(30))), wall.getDiagonalOffset("21"));
        assertEquals(Math.round(200 / Math.sin(Math.toRadians(30))), wall.getDiagonalOffset("11"));
        assertEquals(Math.round(200 / Math.sin(Math.toRadians(30))), wall.getDiagonalOffset("31"));
        assertEquals(Math.round(200 / Math.sin(Math.toRadians(30))), wall.getDiagonalOffset("41"));
        assertEquals(Math.round(200 / Math.sin(Math.toRadians(60))), wall.getDiagonalOffset("42"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(60))), wall.getDiagonalOffset("12"));

        wall = plan.createWall(0, 1000, 0, (float)Math.sqrt(3)*1000, 0, 1);
        wall.setDiagonalWidth(200);
        assertEquals(Math.round(200/Math.sin(Math.toRadians(30))), wall.getDiagonalOffset("32"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(30))), wall.getDiagonalOffset("22"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(60))), wall.getDiagonalOffset("21"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(60))), wall.getDiagonalOffset("11"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(60))), wall.getDiagonalOffset("31"));
        assertEquals(Math.round(200 / Math.sin(Math.toRadians(60))), wall.getDiagonalOffset("41"));
        assertEquals(Math.round(200 / Math.sin(Math.toRadians(30))), wall.getDiagonalOffset("42"));
        assertEquals(Math.round(200/Math.sin(Math.toRadians(30))), wall.getDiagonalOffset("12"));
    }

    @Test
    public void testFormulas() {
        Wall wall;

        wall = plan.createWall(0, 1000, 0, 1000, 0, 1);
        wall.setDiagonalWidth((int)(Math.sqrt(1000*1000 + 1000*1000)/2.0));
        wall.setWallShape(WallShape.DIAGONAL1U);

        assertEquals(0.5, wall.getArea(), 0.1);
    }
}
