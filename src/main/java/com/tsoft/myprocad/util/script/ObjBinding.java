package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.util.SwingTools;
import sun.nio.ch.IOUtil;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * OBJ 3D Model bindings
 */
public class ObjBinding implements JavaScriptBinding {
    /** Returns full file name with generated OBJ data */
    public String generate(String outputFileName, Object[][] vertexes, Object[][] faces) throws IOException {
        outputFileName = System.getProperty("user.home") + File.separator + outputFileName;
        try (PrintWriter out = new PrintWriter(outputFileName)) {
            out.println("# Vertexes");
            for (int i = 0; i < vertexes.length; i ++) {
                out.print("v");
                for (int k = 0; k < vertexes[i].length; k ++) {
                    out.print(" " + vertexes[i][k].toString());
                }
                out.println();
            }

            out.println("# Faces");
            for (int i = 0; i < faces.length; i ++) {
                out.print("f");
                for (int k = 0; k < faces[i].length; k ++) {
                    out.print(" " + faces[i][k].toString());
                }
                out.println();
            }
        }
        return outputFileName;
    }

    @Override
    public String getBindingName() {
        return "obj";
    }
}
