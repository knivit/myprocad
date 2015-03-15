package com.tsoft.myprocad.swing.properties;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;
import com.tsoft.myprocad.model.Pattern;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class PatternComboBoxRenderer extends DefaultCellRenderer {
    @Override
    public Component getListCellRendererComponent(final JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        Pattern pattern = (Pattern)value;
        return super.getListCellRendererComponent(list, pattern, index, isSelected, cellHasFocus);
    }

    @Override
    protected String convertToString(Object value) {
        if (value == null) return "";
        return value.toString();
    }

    @Override
    protected Icon convertToIcon(Object value) {
        if (value == null) return null;
        return new PatternIcon((Pattern)value);
    }

    public static class PatternIcon implements Icon {
        private BufferedImage patternImage;

        PatternIcon(Pattern pattern) {
            patternImage = pattern.getPatternImage(Color.WHITE.getRGB(), Color.BLACK.getRGB());;
        }

        @Override
        public int getIconWidth() {
            int iconWidth = patternImage.getWidth() * 4 + 1;
            return iconWidth;
        }

        @Override
        public int getIconHeight() {
            int iconHeight = patternImage.getHeight() + 2;
            return iconHeight;
        }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2D = (Graphics2D)g;
            for (int i = 0; i < 4; i++) {
                g2D.drawImage(patternImage, x + i * patternImage.getWidth(), y + 1, null);
            }
            g2D.setColor(Color.BLACK);
            g2D.drawRect(x, y, getIconWidth() - 2, getIconHeight() - 1);
        }
    }
}
