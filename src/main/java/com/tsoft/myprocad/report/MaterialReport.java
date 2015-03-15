package com.tsoft.myprocad.report;

public abstract class MaterialReport {
    public abstract String getLocalizedName();

    public abstract String generate(MaterialReportParams params);

    public static MaterialReport[] reports = new MaterialReport[] {
            new MaterialReportLevelTotal(),
            new MaterialReportLevel(),
            new MaterialReportProject(),
            new MaterialReportProjectGroupByLevel()
    };
}
