package com.tsoft.myprocad.util.math;

public class MathFunctionSqrt extends MathFunction {
    @Override
    public Double apply() {
        return Math.sqrt(args.get(0));
    }
}
