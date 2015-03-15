package com.tsoft.myprocad.util.math;

import com.tsoft.myprocad.util.StringUtil;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class Calculator {
    private String textSolution;

    // functions for operations
    private static BiFunction<Double, Double, Double> add = (t, u) -> t + u;
    private static BiFunction<Double, Double, Double> sub = (t, u) -> t - u;
    private static BiFunction<Double, Double, Double> mul = (t, u) -> t * u;
    private static BiFunction<Double, Double, Double> div = (t, u) -> t / u;
    private static BiFunction<Double, Double, Double> pow = Math::pow;

    static class Op {
        int level;
        char op;
        Op(int level, char op) {
            this.level = level;
            this.op = op;
        }
    }

    static class OpFunction {
        int level;
        BiFunction<Double, Double, Double> function;
        OpFunction(int level, BiFunction<Double, Double, Double> function) {
            this.level = level;
            this.function = function;
        }
    }

    private static Map<Character, OpFunction> biFuncs = new HashMap<>();
    static {
        biFuncs.put('+', new OpFunction(1, add));
        biFuncs.put('-', new OpFunction(1, sub));
        biFuncs.put('*', new OpFunction(2, mul));
        biFuncs.put('/', new OpFunction(2, div));
        biFuncs.put('^', new OpFunction(3, pow));
    }

    private static Map<String, MathFunction> funcs = new HashMap<>();


    private static Map<String, Double> consts = new HashMap<>();
    static {
        consts.put("pi", Math.PI);
    }

    /**
     * Return null in case an error
     * getTextSolution() returns a text presentation of the result
     * or an error message, or a complex result (like two roots for
     * a quadratic equation
     *
     * Character ',' should be used only as args delimiter
     * '.' is the only one to use as a decimal point
     */
    public Double calc(String expression) {
        if (StringUtil.isEmpty(expression)) return 0.0;

        // re-create functions as they can keep a state
        funcs.put("sin", new MathFunctionSin());
        funcs.put("cos", new MathFunctionCos());
        funcs.put("tan", new MathFunctionTan());
        funcs.put("sqrt", new MathFunctionSqrt());
        funcs.put("qe", new MathFunctionQuadraticEquation());

        // convert the expression to list of characters and remove spaces
        List<Character> chars = new ArrayList<>();
        for (int i = expression.length() - 1; i >= 0; i--) {
            char ch = Character.toLowerCase(expression.charAt(i));
            if (ch > ' ' && ch < 'z') chars.add(ch);
        }

        Supplier<Stack<Character>> supplier = Stack::new;
        BiConsumer<Stack<Character>, Character> accumulator = (list, value) -> list.add(value);
        BiConsumer<Stack<Character>, Stack<Character>> consumer = (left, right) -> left.addAll(right);
        Stack<Character> in = chars.stream().filter(e -> !e.equals(' ')).collect(supplier, accumulator, consumer);

        Double answer = doCalc(in);
        if (!in.isEmpty()) return null;
        return answer;
    }

    private Double doCalc(Stack<Character> in) {
        Stack<Double> s = new Stack<>();
        Stack<Op> ops = new Stack<>();

        final int order = 2;
        int level = 0;
        char unary;

        while (true) {
            if (!in.isEmpty() && in.peek() == ',') break;

            while (!in.isEmpty() && in.peek() == '(') { level += order; in.pop(); }

            unary = getUnary(in);
            String token = getToken(in);
            Double val = getValue(in, token);
            if (val == null) return null;
            if (unary == '-') val = -val;
            s.push(val);

            boolean isEmpty = false;
            while (!in.isEmpty() && in.peek() == ')') {
                if (level == 0) { isEmpty = true; break; }
                level -= order; in.pop();
            }
            if (!in.isEmpty() && in.peek() == ',') isEmpty = true;

            int opLevel = 0;
            if (!isEmpty && !in.isEmpty()) {
                Character op = in.peek();
                OpFunction func = biFuncs.get(op);
                if (func == null) return null;

                opLevel = func.level + level;
            }

            while (!ops.empty() && ops.peek().level >= opLevel) {
                if (!doOp(s, ops, biFuncs)) return null;
            }

            if (isEmpty || in.isEmpty()) {
                if (level != 0) return null;
                break;
            }

            ops.push(new Op(opLevel, in.pop()));
        }

        return s.peek();
    }

    private char getUnary(Stack<Character> in) {
        if (in.isEmpty()) return ' ';

        char unary = in.peek();
        if (unary == '+' || unary == '-') in.pop();
        if (unary != '-') unary = ' ';
        return unary;
    }

    private Double getValue(Stack<Character> in, String token) {
        Double value = getMathConst(token);
        if (value != null) return value;

        if (isMathFunc(token)) return getMathFunc(in, token);

        try {
            return Double.valueOf(token);
        } catch (Exception ex) { }
        return null;
    }

    private boolean doOp(Stack<Double> s, Stack<Op> ops, Map<Character, OpFunction> funcs) {
        char op = ops.peek().op;
        OpFunction opf = funcs.get(op);
        if (opf == null) return false;

        BiFunction<Double, Double, Double> f = opf.function;

        Double r = s.pop();
        r = f.apply(s.peek(), r);
        s.pop();
        s.push(r);
        ops.pop();

        return true;
    }

    private String getToken(Stack<Character> in) {
        StringBuilder buf = new StringBuilder();
        while (!in.isEmpty()) {
            char ch = Character.toLowerCase(in.peek());
            if (Character.isDigit(ch) || Character.isAlphabetic(ch) || ch == '.') {
                buf.append(ch);
                in.pop();
            } else break;
        }
        return buf.toString();
    }

    private boolean isMathFunc(String token) {
        for (String name : funcs.keySet()) {
            if (name.equals(token)) return true;
        }
        return false;
    }

    private Double getMathFunc(Stack<Character> in, String token) {
        MathFunction fn = funcs.get(token);

        // get args
        if (in.pop() != '(') return null;
        while (!in.isEmpty()) {
            Double arg = doCalc(in);
            fn.addArg(arg);

            if (!in.isEmpty() && in.peek() == ',') in.pop();
            else break;
        }
        if (in.isEmpty() || in.pop() != ')') return null;

        // calc the function
        Double value = fn.apply();
        if (value != null) return value;

        textSolution = fn.getTextSolution();
        return null;
    }

    private Double getMathConst(String token) {
        return consts.get(token);
    }

    public String getTextSolution() { return textSolution; }
}
