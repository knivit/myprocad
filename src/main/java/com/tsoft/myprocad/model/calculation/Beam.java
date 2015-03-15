package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.ProjectItem;
import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.viewcontroller.BeamController;
import com.tsoft.myprocad.viewcontroller.ProjectItemController;

import java.io.IOException;

public class Beam extends ProjectItem {
    private double length = 10;           // Beam length, m
    private double leftSupport = 0;       // Offset to the left support, m (0 .. length)
    private double rightSupport = 10;     // Offset to the right support, m (0 .. length)
    private double elasticStrength = 2e5; // Elastic Strength E, MPa (модуль упругости Юнга E (МПа))
    private double allowableStress = 160; // Allowable Stress, [σ] MPa (допускаемое напряжение [σ] (МПа))

    private MomentList moments = new MomentList(); // Bending moments, kNm
    private ForceList forces = new ForceList();    // Normal Force, kN
    private DistributedForceList distributedForces = new DistributedForceList(); // Distributed Normal Forces, kN/m

    private transient BeamController controller;

    /* Static calculation results */
    public transient BeamSolution[] solutions = new BeamSolution[6];

    // Don't remove this constructor, it is needed for JsonReader
    public Beam() { }

    public double getLength() {
        return length;
    }

    public String validateLength(Double length) {
        if (length == null || length <= 0.001) return "Длина балки не может быть меньше 0.001 м";

        if ((leftSupport < 0) || (leftSupport > length)) return "Левая опора должна быть на балке";

        if ((rightSupport < 0) || (rightSupport > length)) return "Правая опора должна быть на балке";

        for (Moment moment : moments) {
            if (moment.zm >= length) return "Изгибающий момент не может быть вне балки";
        }

        for (Force force : forces) {
            if (force.zs >= length) return "Сосредоточеггая сила не может быть вне балки";
        }

        for (DistributedForce distributedForce : distributedForces) {
            if (distributedForce.z2 >= length) return "Распределенная нагрузка не может быть вне балки";
        }

        return null;
    }

    public void setLength(double length) {
        if (this.length == length) return;

        this.length = length;
        controller.beamChanged();
    }

    public double getLeftSupport() {
        return leftSupport;
    }

    public String validateLeftSupport(double leftSupport) {
        if ((leftSupport < 0) || (leftSupport > length)) return "Левая опора должна быть на балке";
        return null;
    }

    public void setLeftSupport(double leftSupport) {
        if (this.leftSupport == leftSupport) return;

        if (rightSupport < leftSupport) {
            double tmp = leftSupport;
            leftSupport = rightSupport;
            rightSupport = tmp;
        }

        this.leftSupport = leftSupport;
        controller.beamChanged();
    }

    public double getRightSupport() {
        return rightSupport;
    }

    public String validateRightSupport(double rightSupport) {
        if ((rightSupport < 0) || (rightSupport > length)) return "Правая опора должна быть на балке";
        return null;
    }

    public void setRightSupport(double rightSupport) {
        if (this.rightSupport == rightSupport) return;

        if (rightSupport < leftSupport) {
            double tmp = leftSupport;
            leftSupport = rightSupport;
            rightSupport = tmp;
        }

        this.rightSupport = rightSupport;
        controller.beamChanged();
    }

    public double getElasticStrength() {
        return elasticStrength;
    }

    public void setElasticStrength(double elasticStrength) {
        if (this.elasticStrength == elasticStrength) return;
        if (elasticStrength <= 0.001) elasticStrength = 0.001;

        this.elasticStrength = elasticStrength;
        controller.beamChanged();
    }

    public double getAllowableStress() {
        return allowableStress;
    }

    public void setAllowableStress(double allowableStress) {
        if (this.allowableStress == allowableStress) return;
        if (allowableStress <= 0.001) allowableStress = 0.001;

        this.allowableStress = allowableStress;
        controller.beamChanged();
    }

    public MomentList getMoments() {
        return moments;
    }

    public void setMoments(MomentList moments) {
        if (this.moments.equals(moments)) return;
        this.moments = moments;
        controller.beamChanged();
    }

    public String validateMoments(TableDialogPanelSupport<Moment> moments) {
        int row = 1;
        for (Moment moment : (MomentList)moments) {
            if (moment.vm == 0) return String.format("Строка %d. Момент равен нулю - не задаём его", row);
            if ((moment.zm < 0) || (moment.zm > getLength())) return String.format("Строка %d. Момент должен быть на балке", row);
            row ++;
        }
        return null;
    }

    public ForceList getForces() {
        return forces;
    }

    public void setForces(ForceList forces) {
        if (this.forces.equals(forces)) return;
        this.forces = forces;
        controller.beamChanged();
    }

    public String validateForces(TableDialogPanelSupport<Force> forces) {
        int row = 1;
        for (Force force : (ForceList)forces) {
            if (force.vs == 0) return L10.get(L10.CALCULATION_BEAM_ERROR_FORCE_IS_ZERO, row);
            if ((force.zs < 0) || (force.zs > getLength())) return L10.get(L10.CALCULATION_BEAM_ERROR_FORCE_IS_OUT, row);
            row ++;
        }
        return null;
    }

    public DistributedForceList getDistributedForces() {
        return distributedForces;
    }

    public void setDistributedForces(DistributedForceList distributedForces) {
        if (this.distributedForces.equals(distributedForces)) return;
        this.distributedForces = distributedForces;
        controller.beamChanged();
    }

    public String validateDistributedForces(TableDialogPanelSupport<DistributedForce> distributedForces) {
        int row = 1;
        for (DistributedForce df : (DistributedForceList)distributedForces) {
            df.normalize();

            if ((df.z1 < 0) || (df.z1 > getLength())) return String.format("Строка %d. Распределённая нагрузка должна быть на балке", row);
            if ((df.z2 < 0) || (df.z2 > getLength())) return String.format("Строка %d. Распределённая нагрузка должна быть на балке", row);
            if (df.z1 == df.z2) return String.format("Строка %d. Интервал нулевой - не задаём нагрузку", row);
            if ((df.q1 == 0) && (df.q2 == 0)) return String.format("Строка %d. Распределённая нагрузка равна нулю - не задаём её", row);

            row ++;
        }
        return null;
    }

    /** For MMLib */
    public double getVsc() {
        double vsc = 0;

        for (Moment moment : moments) {
            vsc = Math.max(vsc, Math.abs(moment.vm));
        }

        for (Force force : forces) {
            vsc = Math.max(vsc, Math.abs(force.vs));
        }

        for (DistributedForce distributedForce : distributedForces) {
            vsc = Math.max(vsc, Math.abs(distributedForce.q1));
            vsc = Math.max(vsc, Math.abs(distributedForce.q2));
        }
        return vsc;
    }

    @Override
    public ProjectItemController getController() {
        if (controller == null) {
            controller = BeamController.createBeamController(this);
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
                .write("length", length)
                .write("leftSupport", leftSupport)
                .write("rightSupport", rightSupport)
                .write("elasticStrength", elasticStrength)
                .write("allowableStress", allowableStress)
                .write("moments", moments)
                .write("forces", forces)
                .write("distributedForces", distributedForces);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        moments = new MomentList();
        forces = new ForceList();
        distributedForces = new DistributedForceList();

        super.fromJson(reader);
        reader
                .defDouble("length", ((value) -> length = value))
                .defDouble("leftSupport", ((value) -> leftSupport = value))
                .defDouble("rightSupport", ((value) -> rightSupport = value))
                .defDouble("elasticStrength", ((value) -> elasticStrength = value))
                .defDouble("allowableStress", ((value) -> allowableStress = value))
                .defCollection("moments", Moment::new, ((value) -> moments.add((Moment) value)))
                .defCollection("forces", Force::new, ((value) -> forces.add((Force) value)))
                .defCollection("distributedForces", DistributedForce::new, ((value) -> distributedForces.add((DistributedForce) value)))
                .read();
    }
}
