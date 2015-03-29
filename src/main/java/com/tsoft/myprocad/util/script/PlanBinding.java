package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.model.Beam;
import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.Wall;

import javax.script.ScriptException;
import java.io.File;

public class PlanBinding implements JavaScriptBinding {
    private Plan plan;

    public PlanBinding(Plan plan) {
        this.plan = plan;
    }

    public JavaScriptBinding addWall(int x1, int y1, int z1, int x2, int y2, int z2) throws ScriptException {
        validateCoordinates(x1, y1, z1, x2, y2, z2);
        Wall wall = plan.createWall(x1, y1, z1, x2, y2, z2);
        return new WallBinding(wall);
    }

    public JavaScriptBinding addBeam(int x1, int y1, int z1, int x2, int y2, int z2, int width, int height) throws ScriptException {
        validateCoordinates(x1, y1, z1, x2, y2, z2);
        Beam beam = plan.createBeam(x1, y1, z1, x2, y2, z2, width, height);
        return new BeamBinding(beam);
    }

    public void exportToObjFile(String fileName) {
        String userHome = System.getProperty("user.home") + File.separator;
        plan.getController().exportToObjFile(userHome + fileName);
    }

    private void validateCoordinates(int ... coords) throws ScriptException {
        if (coords == null) return;

        for (int coord : coords) {
            String error = Item.validateCoordinate(coord);
            if (error != null) throw new ScriptException(error);
        }
    }

    @Override
    public String getBindingName() {
        return "plan";
    }
}
