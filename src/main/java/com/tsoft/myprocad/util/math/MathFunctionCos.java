package com.tsoft.myprocad.util.math;

public class MathFunctionCos extends MathFunction {
    @Override
    public Double apply() {
        return Math.cos(Math.toRadians(args.get(0)));
    }
}
