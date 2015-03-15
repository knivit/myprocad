package com.tsoft.myprocad.model.calculation;

import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.StringUtil;

public class RightTriangle {
    public static RightTriangle entity = new RightTriangle();

    private Integer aLeg;
    private Integer bLeg;
    private Integer cHypotenuse;

    public Integer getALeg() { return aLeg; }

    public Integer getBLeg() { return bLeg; }

    public Integer getCHypotenuse() { return cHypotenuse; }

    public Integer getArea() {
        if (aLeg == null || bLeg == null) return null;

        return (int)Math.round(aLeg*bLeg / 2.0);
    }

    public String getAreaM2() {
        if (aLeg == null || bLeg == null) return null;

        return StringUtil.toString(aLeg * bLeg / 2.0 / (1000 * 1000), 3);
    }

    public Integer getAAngle() {
        if (aLeg != null && cHypotenuse != null) return (int)Math.round(Math.toDegrees(Math.asin(1.0 * aLeg / cHypotenuse)));
        if (aLeg != null && bLeg != null) return (int)Math.round(Math.toDegrees(Math.atan(1.0 * aLeg / bLeg)));
        return null;
    }

    public Integer getBAngle() {
        if (bLeg != null && cHypotenuse != null) return (int)Math.round(Math.toDegrees(Math.asin(1.0 * bLeg / cHypotenuse)));
        if (aLeg != null && bLeg != null) return (int)Math.round(Math.toDegrees(Math.atan(1.0 * bLeg / aLeg)));
        return null;
    }

    public Integer getPerimeter() {
        if (aLeg != null && bLeg != null && cHypotenuse != null) return aLeg + bLeg + cHypotenuse;
        return null;
    }

    public void setALeg(Integer aLeg) {
        boolean isModified = !ObjectUtil.equals(aLeg, this.aLeg);
        this.aLeg = aLeg;
        if (aLeg == null || !isModified) return;

        if (bLeg != null) {
            cHypotenuse = (int)Math.round(Math.sqrt(aLeg*aLeg + bLeg*bLeg));
            return;
        }

        if (cHypotenuse != null) {
            bLeg = (int)Math.round(Math.sqrt(cHypotenuse*cHypotenuse - aLeg*aLeg));
            return;
        }
    }

    public void setBLeg(Integer bLeg) {
        boolean isModified = !ObjectUtil.equals(bLeg, this.bLeg);
        this.bLeg = bLeg;
        if (bLeg == null || !isModified) return;

        if (aLeg != null) {
            cHypotenuse = (int)Math.round(Math.sqrt(aLeg*aLeg + bLeg*bLeg));
            return;
        }

        if (cHypotenuse != null) {
            aLeg = (int)Math.round(Math.sqrt(cHypotenuse*cHypotenuse - bLeg*bLeg));
            return;
        }
    }

    public void setCHypotenuse(Integer cHypotenuse) {
        boolean isModified = !ObjectUtil.equals(cHypotenuse, this.cHypotenuse);
        this.cHypotenuse = cHypotenuse;
        if (cHypotenuse == null || !isModified) return;

        if (aLeg == null && bLeg != null) {
            aLeg = (int)Math.round(Math.sqrt(cHypotenuse*cHypotenuse - bLeg*bLeg));
            return;
        }

        bLeg = (int)Math.round(Math.sqrt(cHypotenuse * cHypotenuse - aLeg*aLeg));
    }
}
