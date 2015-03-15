package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.ProjectItem;
import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.viewcontroller.ProjectItemController;
import com.tsoft.myprocad.viewcontroller.WoodBeamController;

import java.io.IOException;
import java.util.List;

public class WoodBeam extends ProjectItem {
    private double l = 4000; // длина пролета, мм
    private double b = 600; // расстояние между балками, мм
    private Load1List permanentLoad = new Load1List(); // постоянная нагрузка (т.е. состав перекрытия)
    private Load2List temporaryLoad = new Load2List(); // временная нагрузка (люди, перегородки, снеговая и ветровая нагрузки)
    private int sagId = BeamSag.ATTIC_LAP.getId(); // элемент здания (для определения максимального прогиба)
    private boolean calcAll; // рассчитать по всем типоразмерам

    private transient WoodBeamController controller;
    private transient BeamSag sag;

    public WoodBeam() {
        // постоянная нагрузка
        permanentLoad.add(new Load1("Настил из половой доски", 600, 0.05));
        permanentLoad.add(new Load1("Звукоизоляция из урсы", 15, 0.1));
        permanentLoad.add(new Load1("Необрезная доска", 600, 0.019));
        permanentLoad.add(new Load1("Гипсокартон", 1000, 0.01));

        // временная нагрузка
        temporaryLoad.add(new Load2("Нормативная нагрузка на межэтажное перекрытие", 250));
        temporaryLoad.add(new Load2("Нормативная нагрузка от веса перегородок", 75));
        temporaryLoad.add(new Load2("Снеговая нагрузка", 100));
        temporaryLoad.add(new Load2("Ветровая нагрузка", 20));
    }

    public double getL() { return l; }

    public String validateL(Double l) {
        if (l == null || l < 1 || l > 6000) return "Длина деревянной балки должна быть в диапазоне [1, 6000] мм";
        return null;
    }

    public void setL(double l) {
        if (this.l == l) return;

        this.l = l;
        controller.woodBeamChanged();
    }

    public double getB() { return b; }

    public String validateB(Double b) {
        if (b == null || b < 1 || b > 3000) return "Расстояние между деревянными балками должно быть в диапазоне [1, 3000] мм";
        return null;
    }

    public void setB(double b) {
        if (this.b == b) return;

        this.b = b;
        controller.woodBeamChanged();
    }

    public Load1List getPermanentLoad() { return permanentLoad; }

    public String validatePermanentLoad(List<Load1> permanentLoad) {
        int row = 1;
        for (Load1 load : permanentLoad) {
            if (StringUtil.isEmpty(load.name)) return L10.get(L10.CALCULATION_WOOD_BEAM_ERROR_EMPTY_NAME, row);
            if (load.density < 1 || load.density > 3000) return L10.get(L10.CALCULATION_WOOD_BEAM_ERROR_INVALID_DENSITY, row);
            if (load.h < 0.001 || load.h > 3) return L10.get(L10.CALCULATION_WOOD_BEAM_ERROR_INVALID_H, row);
            row ++;
        }
        return null;
    }

    public void setPermanentLoad(Load1List permanentLoad) {
        if (this.permanentLoad.equals(permanentLoad)) return;
        this.permanentLoad = permanentLoad;
        controller.woodBeamChanged();
    }

    public Load2List getTemporaryLoad() { return temporaryLoad; }

    public void setTemporaryLoad(Load2List temporaryLoad) {
        if (this.temporaryLoad.equals(temporaryLoad)) return;
        this.temporaryLoad = temporaryLoad;
        controller.woodBeamChanged();
    }

    public BeamSag getSag() {
        if (sag == null) sag = BeamSag.findById(sagId);
        return sag;
    }

    public void setSag(BeamSag sag) {
        if (getSag().equals(sag)) return;
        sagId = sag.getId();
        this.sag = sag;
        controller.woodBeamChanged();
    }

    public boolean isCalcAll() {
        return calcAll;
    }

    public void setCalcAll(boolean value) {
        if (calcAll == value) return;
        this.calcAll = value;
        controller.woodBeamChanged();
    }

    @Override
    public ProjectItemController getController() {
        if (controller == null) {
            controller = WoodBeamController.createWoodBeamController(this);
        }
        return controller;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);
        writer
                .write("l", l)
                .write("b", b)
                .write("permanentLoad", permanentLoad)
                .write("temporaryLoad", temporaryLoad)
                .write("sagId", sagId)
                .write("calcAll", calcAll);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        permanentLoad = new Load1List();
        temporaryLoad = new Load2List();

        super.fromJson(reader);
        reader
                .defDouble("l", ((value) -> l = value))
                .defDouble("b", ((value) -> b = value))
                .defCollection("permanentLoad", Load1::new, ((value) -> permanentLoad.add((Load1) value)))
                .defCollection("temporaryLoad", Load2::new, ((value) -> temporaryLoad.add((Load2)value)))
                .defInteger("sagId", ((value) -> sagId = value))
                .defBoolean("calcAll", ((value) -> calcAll = value))
                .read();
    }
}
