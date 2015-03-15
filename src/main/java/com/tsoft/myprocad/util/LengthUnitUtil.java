package com.tsoft.myprocad.util;

public class LengthUnitUtil {
    public static float centimeterToInch(float length) {
        return length / 2.54f;
    }

    public static float inchToCentimeter(float length) {
        return length * 2.54f;
    }

    // In mm
    public static float getMaximumLength() {
        return 100000f; // 100 m
    }
}
