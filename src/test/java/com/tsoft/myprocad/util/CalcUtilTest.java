package com.tsoft.myprocad.util;

import com.tsoft.myprocad.util.math.Calculator;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CalcUtilTest {
    public static final double DELTA = 0.000001;

    @Test
    public void calculator() {
        // positive
        Assert.assertEquals(2.0, calc("2"), DELTA);
        Assert.assertEquals(-2.0, calc("-2"), DELTA);
        Assert.assertEquals(2.0, calc("+2"), DELTA);

        Assert.assertEquals(2.0, calc("1+1"), DELTA);
        Assert.assertEquals(2.0, calc("1 + 1"), DELTA);
        Assert.assertEquals(2.0, calc("1 + 1"), DELTA);
        Assert.assertEquals(1000.0, calc("1 000"), DELTA);

        Assert.assertEquals(Math.tan(Math.toRadians(45)), calc("tan(45)"), DELTA);
        Assert.assertEquals(Math.tan(Math.toRadians(45)), calc("tan(5*9)"), DELTA);
        Assert.assertEquals(Math.tan(Math.toRadians(45)), calc("tan((5*9))"), DELTA);
        Assert.assertEquals(Math.tan(Math.toRadians(45)), calc("tan(1+(5*9)-1)"), DELTA);

        Assert.assertEquals(Math.sqrt(5.3 * 5.3 + 2.7 * 2.7), calc("sqrt(5.3^2 + 2.7^2)"), DELTA);
        Assert.assertEquals(0.0, calc("qe(1,0,0)"), DELTA);
        Assert.assertEquals(0.0, calc("qe(1*2-(2-1), 0.0+0.0 ,0)"), DELTA);
        Assert.assertEquals(0.0, calc("qe(1)"), DELTA);

        Assert.assertEquals(Math.PI, calc("pi"), DELTA);
        Assert.assertEquals(2 * (-Math.PI / 1), calc("2*(-pi/1)"), DELTA);
        Assert.assertEquals(2 * -Math.PI, calc("2*(-pi/1)"), DELTA);

        // negative
        Assert.assertEquals(null, calc("("));
        Assert.assertEquals(null, calc(")"));
        Assert.assertEquals(null, calc("(1"));
        Assert.assertEquals(null, calc("1+"));
        Assert.assertEquals(null, calc("+1)"));
        Assert.assertEquals(null, calc("()"));
    }

    private Double calc(String expr) {
        Calculator calculator = new Calculator();
        return calculator.calc(expr);
    }
}
