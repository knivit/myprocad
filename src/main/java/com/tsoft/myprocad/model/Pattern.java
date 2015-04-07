package com.tsoft.myprocad.model;

import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.util.Content;
import com.tsoft.myprocad.util.ResourceURLContent;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.RGBImageFilter;
import java.io.IOException;
import java.io.InputStream;

public enum Pattern {
    FOREGROUND(1, "foreground"),
    REVERSED_HATCH_UP(2, "reversedHatchUp"),
    REVERSED_HATCH_DOWN(3, "reversedHatchDown"),
    REVERSED_CROSS_HATCH(4, "reversedCrossHatch"),
    BACKGROUND(5, "background"),
    HATCH_UP(6, "hatchUp"),
    HATCH_DOWN(7, "hatchDown"),
    CROSS_HATCH(8, "crossHatch"),
    SAND(9, "sand"),
    BRICK(10, "brick"),
    DOTTED_VERTICAL(11, "dottedVertical"),
    DOTTED_HORIZONTAL(12, "dottedHorizontal");

    private int id;
    private String resourceName;

    private String resourceFileName;
    private Content image;
    private BufferedImage bufferedImage;

    Pattern(int id, String resourceName) {
        this.id = id;
        this.resourceName = resourceName;

        resourceFileName = "resources/patterns/" + resourceName + ".png";
    }

    public int getId() { return id; }

    public String getResourceName() {
        return resourceName;
    }

    public Content getImage() {
        if (image == null) {
            image = new ResourceURLContent(MyProCAD.class, resourceFileName);
        }
        return image;
    }

    public static Pattern findById(int patternId) {
        for (Pattern pattern : values()) {
            if (pattern.id == patternId) return pattern;
        }
        return null;
    }

    /**
     * Returns the image matching a given pattern.
     */
    public BufferedImage getPatternImage(Color backgroundColor, Color foregroundColor) {
        BufferedImage image = new BufferedImage(10, 10, BufferedImage.TYPE_INT_RGB);
        Graphics2D imageGraphics = (Graphics2D)image.getGraphics();
        imageGraphics.setColor(backgroundColor);
        imageGraphics.fillRect(0, 0, image.getWidth(), image.getHeight());

        // Get pattern image from cache
        if (bufferedImage == null) {
            try (InputStream imageInput = getImage().openStream()) {
                bufferedImage = ImageIO.read(imageInput);
            } catch (IOException ex) {
                throw new IllegalArgumentException("Can't read pattern image " + resourceFileName);
            }
        }

        // Draw the pattern image with foreground color
        final int foregroundColorRgb = foregroundColor.getRGB() & 0xFFFFFF;
        imageGraphics.drawImage(Toolkit.getDefaultToolkit().createImage(
                new FilteredImageSource(bufferedImage.getSource(),
                        new RGBImageFilter() {
                            {
                                this.canFilterIndexColorModel = true;
                            }

                            @Override
                            public int filterRGB(int x, int y, int argb) {
                                // Always use foreground color and alpha
                                return (argb & 0xFF000000) | foregroundColorRgb;
                            }
                        })), 0, 0, null);
        imageGraphics.dispose();
        return image;
    }

    public static Pattern findByName(String patternName) {
        for (Pattern pattern : values()) {
            if (pattern.resourceName.equalsIgnoreCase(patternName) ||
                    pattern.name().equalsIgnoreCase(patternName)) return pattern;
        }
        return null;
    }

    @Override
    public String toString() { return resourceName; }
}
