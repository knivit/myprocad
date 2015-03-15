package com.tsoft.myprocad.report;

import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.Project;

public class MaterialReportParams {
    public Project project;
    public Plan plan;

    public MaterialReportParams(Project project, Plan plan) {
        this.project = project;
        this.plan = plan;
    }
}
