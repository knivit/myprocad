package com.tsoft.myprocad.model;

import java.awt.*;

public class WallPattern {
    private int patternId;
    private Color backgroundColor;
    private Color foregroundColor;

    public WallPattern(int patternId, Color backgroundColor, Color foregroundColor) {
        this.patternId = patternId;
        this.backgroundColor = backgroundColor;
        this.foregroundColor = foregroundColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WallPattern that = (WallPattern) o;

        if (patternId != that.patternId) return false;
        if (!backgroundColor.equals(that.backgroundColor)) return false;
        if (!foregroundColor.equals(that.foregroundColor)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = patternId;
        result = 31 * result + backgroundColor.hashCode();
        result = 31 * result + foregroundColor.hashCode();
        return result;
    }
}
