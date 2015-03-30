package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.MyProCAD;
import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class JavaScript {
    private ScriptEngine js;

    public JavaScript() {
        js = new ScriptEngineManager().getEngineByName("javascript");
    }

    public Object execute(String command, OutputBinding output) throws ScriptException {
        addBinding(output);
        return js.eval(command);
    }

    public void executeScript(String resourceFileName, OutputBinding output) throws IOException, ScriptException {
        addBinding(output);
        loadLibrary(resourceFileName);
    }

    public void addBinding(JavaScriptBinding binding) {
        Bindings bindings = js.getBindings(ScriptContext.ENGINE_SCOPE);
        bindings.put(binding.getBindingName(), binding);
    }

    public void loadLibrary(String resourceFileName) throws IOException, ScriptException {
        try (InputStream is = MyProCAD.class.getResourceAsStream(resourceFileName)) {
            if (is == null) throw new IOException("File " + resourceFileName + " doesn't exist");
            js.eval(new InputStreamReader(is));
        }
    }

    public Object getVariable(String name) {
        int pos = name.indexOf('.');
        if (pos == -1) return js.get(name);

        String objName = name.substring(0, pos);
        String memberName = name.substring(pos + 1);
        ScriptObjectMirror som = (ScriptObjectMirror)getVariable(objName);
        return som.getMember(memberName);
    }

    public void printVariables(OutputBinding output) {
        Bindings bs = js.getBindings(ScriptContext.ENGINE_SCOPE);
        for (String name : bs.keySet()) {
            Object value = bs.get(name);

            if (value instanceof ScriptObjectMirror) {
                ScriptObjectMirror mirror = (ScriptObjectMirror)value;
                if (!mirror.isArray()) continue;

                // TODO print arrays
                continue;
            }

      //      boolean isNumber = (value instanceof Double) || (value instanceof Integer);
       //     boolean isString = value instanceof String;
      //      if (isNumber || isString) {
                if (name.startsWith("$$CALC")) ;
                else if (name.startsWith("$$")) output.print(value.toString());
                else {
                    int pos = name.indexOf('$');
                    String ei = "";
                    if (pos != -1) {
                        ei = name.substring(pos + 1);
                        name = name.substring(0, pos);

                        pos = ei.indexOf("_на_");
                        if (pos != -1) ei = ei.substring(0, pos) + "/" + ei.substring(pos + 4);
                        pos = ei.indexOf("_за_");
                        if (pos != -1) ei = ei.substring(0, pos) + "/" + ei.substring(pos + 4);
                        ei = ei.replace('_', '*');
                    }
                    name = name.replace('_', ' ');
                    output.print(name + " = " + value.toString() + " " + ei);
                }
         //   }
        }
    }
}
