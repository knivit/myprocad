package com.tsoft.myprocad.util.math;

import java.util.ArrayList;
import java.util.List;

public abstract class MathFunction {
    protected List<Double> args = new ArrayList<>();
    protected String textSolution;

    public abstract Double apply();

    public String getTextSolution() { return textSolution; }

    public void addArg(Double arg) {
        args.add(arg);
    }
}
