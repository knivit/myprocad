package com.tsoft.myprocad.model;

public class ItemPoint {
    public int x, y;

    public ItemPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void rotate(int centerX, int centerY, int degree) {
        // Translate to origin
        int x1 = x - centerX;
        int y1 = y - centerY;

        // Apply rotation
        double angle = Math.toRadians(degree);
        double tx1 = x1 * Math.cos(angle) - y1 * Math.sin(angle);
        double ty1 = x1 * Math.sin(angle) + y1 * Math.cos(angle);

        // Translate back
        x = (int)Math.round(tx1 + centerX);
        y = (int)Math.round(ty1 + centerY);
    }
}
