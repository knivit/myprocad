package com.tsoft.myprocad.util;

public class StringUtil {
    private StringUtil() { }

    public static String replaceAll(String str, String pattern, String value) {
        if (str == null) return str;
        return str.replace(pattern, value);
    }

    public static boolean isEmpty(String text) {
        return (text == null) || text.isEmpty();
    }

    public static String toString(double val, int dec) {
        String res = String.format("%." + dec + "f", val);
        if (res.endsWith(",000")) return res.substring(0, res.length() - 4);
        if (res.endsWith(",00")) return res.substring(0, res.length() - 3);
        if (res.endsWith(",0")) return res.substring(0, res.length() - 2);
        if (res.endsWith(",")) return res.substring(0, res.length() - 1);
        return res;
    }
}
