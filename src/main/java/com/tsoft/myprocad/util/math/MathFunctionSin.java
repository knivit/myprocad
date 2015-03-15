package com.tsoft.myprocad.util.math;

public class MathFunctionSin extends MathFunction {
    @Override
    public Double apply() {
        return Math.sin(Math.toRadians(args.get(0)));
    }
}
