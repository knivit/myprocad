package com.tsoft.myprocad.report;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Plan;

public class MaterialReportProjectGroupByLevel extends MaterialReport {
    @Override
    public String getLocalizedName() {
        return L10.get(L10.MATERIAL_REPORT_GROUP_BY_LEVEL);
    }

    @Override
    public String generate(MaterialReportParams params) {
        Plan plan = params.plan;

        MaterialData materialData = new MaterialData(plan.getWalls());

        StringBuilder buf = new StringBuilder();
        buf.append(plan.getName()).append('\n');
        buf.append("+----------------------------------------+----------+----------+----------+----------+------+------+\n");
        buf.append("|                Material                |Volume,m3 |  +2%,m3  |Weight,tn |  +2%,tn  |Amount|  +2% |\n");
        buf.append("+----------------------------------------+----------+----------+----------+----------+------+------+\n");
        for (MaterialGroupData mgd : materialData.materialGroups) {
            buf.append(String.format("|%-40s|%10.3f|%10.3f|%10.3f|%10.3f|%6d|%6d|\n",
                mgd.material.getName(),
                mgd.total.getVolume(), (mgd.total.getVolume() * 1.02),
                mgd.total.getWeight(), (mgd.total.getWeight() * 1.02),
                mgd.total.amount, (int)Math.ceil(mgd.total.amount * 1.02)));
            buf.append("+----------------------------------------+----------+----------+----------+----------+------+------+\n");

            for (MaterialRecord record : mgd.records) {
                buf.append(String.format("|%40s|%10.3f|%10.3f|%10.3f|%10.3f|%6d|%6d|\n",
                        record.size,
                        record.getVolume(), (record.getVolume() * 1.02),
                        record.getWeight(), (record.getWeight() * 1.02),
                        record.amount, (int)Math.ceil(record.amount * 1.02)));
            }
        }
        buf.append("+----------------------------------------+----------+----------+----------+----------+------+------+\n");
        return buf.toString();
    }
}
