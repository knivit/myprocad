package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.util.StringUtil;

public class Triangle {
    public static Triangle entity = new Triangle();

    private Integer aLeg;
    private Integer bLeg;
    private Integer cLeg;

    public Integer getALeg() { return aLeg; }

    public Integer getBLeg() { return bLeg; }

    public Integer getCLeg() { return cLeg; }

    /** Heron's formula */
    public Integer getArea() {
        if (aLeg == null || bLeg == null || cLeg == null) return null;

        double p = (aLeg + bLeg + cLeg) / 2.0;
        return (int)Math.sqrt(p * (p - aLeg) * (p - bLeg) * (p - cLeg));
    }

    public String getAreaM2() {
        Integer s = getArea();
        if (s == null) return null;

        return StringUtil.toString(s / (1000 * 1000), 3);
    }

    /** Law of cosines */
    public Integer getAAngle() {
        if (aLeg != null && bLeg != null && cLeg != null) {
            return (int)Math.round(Math.toDegrees(Math.acos((bLeg*bLeg + cLeg*cLeg - aLeg*aLeg) / (2.0 * bLeg * cLeg))));
        }
        return null;
    }

    public Integer getBAngle() {
        if (aLeg != null && bLeg != null && cLeg != null) {
            return (int)Math.round(Math.toDegrees(Math.acos((aLeg*aLeg + cLeg*cLeg - bLeg*bLeg) / (2.0 * aLeg * cLeg))));
        }
        return null;
    }

    public Integer getCAngle() {
        if (aLeg != null && bLeg != null && cLeg != null) {
            return (int)Math.round(Math.toDegrees(Math.acos((aLeg*aLeg + bLeg*bLeg - cLeg*cLeg) / (2.0 * aLeg * bLeg))));
        }
        return null;
    }

    public Integer getPerimeter() {
        if (aLeg != null && bLeg != null && cLeg != null) return aLeg + bLeg + cLeg;
        return null;
    }

    public void setALeg(Integer aLeg) {
        this.aLeg = aLeg;
    }

    public void setBLeg(Integer bLeg) {
        this.bLeg = bLeg;
    }

    public void setCLeg(Integer cLeg) {
        this.cLeg = cLeg;
    }
}
