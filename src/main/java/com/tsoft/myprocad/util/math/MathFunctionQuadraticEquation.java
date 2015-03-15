package com.tsoft.myprocad.util.math;

import com.tsoft.myprocad.util.StringUtil;

import java.util.Iterator;

public class MathFunctionQuadraticEquation extends MathFunction {
    @Override
    public Double apply() {
        double a = 0, b = 0, c = 0;
        Iterator<Double> iterator = args.iterator();
        if (iterator.hasNext()) a = iterator.next();
        if (iterator.hasNext()) b = iterator.next();
        if (iterator.hasNext()) c = iterator.next();

        double d = b * b - 4 * a * c;
        if (d > 0) {
            double x1 = (-b + Math.sqrt(d)) / (2 * a);
            double x2 = (-b - Math.sqrt(d)) / (2 * a);

            textSolution = "x1=" + StringUtil.toString(x1, 3) + ", x2=" + StringUtil.toString(x2, 3);
            return null;
        }

        if (d == 0) {
            return -b / (2 * a);
        }

        return null;
    }
}
