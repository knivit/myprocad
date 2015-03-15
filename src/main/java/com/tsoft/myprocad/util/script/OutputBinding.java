package com.tsoft.myprocad.util.script;

public abstract class OutputBinding implements JavaScriptBinding {
    private JavaScript js;

    public OutputBinding(JavaScript js) { this.js = js; }

    public void print(String format, Object... args) {
        // Formatter doesn't support both Integer and Float as a one conversion
        // So, will use Float conversion (%f) for Integers also
        // but that needs Integers to be converted to Floats
        /* int n = 0;
        Object[] cargs = new Object[args.length];
        for (Object arg : args) {
            if (arg instanceof Integer) cargs[n] = ((Integer)arg).floatValue();
            else cargs[n] = arg;
            n ++;
        } */

        String text = String.format(format, args);
        print(text);
    }

    public abstract void print(String text);

    public void printVariables() {
        js.printVariables(this);
    }

    public void error(String text) {
        print(text);
    }

    public void clear() { }

    public String getBindingName() {
        return "output";
    }
}
