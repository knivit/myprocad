package com.tsoft.myprocad.util.math;

public class MathFunctionTan extends MathFunction {
    @Override
    public Double apply() {
        return Math.tan(Math.toRadians(args.get(0)));
    }
}
