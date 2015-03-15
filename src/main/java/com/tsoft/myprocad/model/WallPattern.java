package com.tsoft.myprocad.model;

public class WallPattern {
    private int patternId;
    private int backgroundColor;
    private int foregroundColor;

    public WallPattern(int patternId, int backgroundColor, int foregroundColor) {
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
        if (backgroundColor != that.backgroundColor) return false;
        if (foregroundColor != that.foregroundColor) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = patternId;
        result = 31 * result + backgroundColor;
        result = 31 * result + foregroundColor;
        return result;
    }
}
