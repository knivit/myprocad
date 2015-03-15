package com.tsoft.myprocad.model;

import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

public class ItemPoints {
    public class Point {
        public float x;
        public float y;

        Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private List<Point> points = new ArrayList<>();

    public ItemPoints() { }

    public ItemPoints add(float x, float y) {
        Point point = new Point(x, y);
        points.add(point);
        return this;
    }

    public Point get(int index) {
        return points.get(index);
    }

    public int size() {
        return points.size();
    }

    /**
     * Returns the shape matching the coordinates in <code>points</code> array.
     */
    public GeneralPath getShape() {
        GeneralPath path = new GeneralPath();
        path.moveTo(points.get(0).x, points.get(0).y);
        for (int i = 1; i < points.size(); i++) {
            path.lineTo(points.get(i).x, points.get(i).y);
        }
        path.closePath();
        return path;
    }

    public float calcArea() {
        float sum = 0;
        for (int i = 0; i < points.size() -1; i++) {
            sum = sum + points.get(i).x*points.get(i+1).y - points.get(i).y*points.get(i+1).x;
        }
        return sum / 2;
    }
}
