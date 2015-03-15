package com.tsoft.myprocad.model.calculation;

import java.awt.*;
import java.awt.image.BufferedImage;

public class BeamSolution {
    private BufferedImage image;

    private Graphics2D g2d;

    public BeamSolution() {
        image = new BufferedImage(800, 500, BufferedImage.TYPE_4BYTE_ABGR);
        g2d = image.createGraphics();
    }

    public Graphics2D getG2d() { return g2d; }

    public BufferedImage getImage() { return image; }
}
