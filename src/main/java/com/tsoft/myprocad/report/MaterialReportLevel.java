package com.tsoft.myprocad.report;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Plan;

public class MaterialReportLevel extends MaterialReport {
    @Override
    public String getLocalizedName() {
        return L10.get(L10.MATERIAL_REPORT_LEVEL);
    }

    @Override
    public String generate(MaterialReportParams params) {
        Plan plan = params.plan;

        MaterialData materialData = new MaterialData(plan.getLevelWalls());

        StringBuilder buf = new StringBuilder();
        buf.append(plan.getName()).append('\n');;
        buf.append("+----------------------------------------+----------+----------+------+\n");
        buf.append(String.format("|%40s|%10s|%10s|%6s|\n",
                L10.get(L10.MATERIAL_REPORT_MATERIAL_COLUMN),
                L10.get(L10.MATERIAL_REPORT_VOLUME_COLUMN),
                L10.get(L10.MATERIAL_REPORT_WEIGHT_COLUMN),
                L10.get(L10.MATERIAL_REPORT_AMOUNT_COLUMN)));
        buf.append("+----------------------------------------+----------+----------+------+\n");
        for (MaterialGroupData mgd : materialData.materialGroups) {
            buf.append(String.format("|%-40s|%10.3f|%10.3f|%6d|\n",
                mgd.material.getName(),
                mgd.total.getVolume(),
                mgd.total.getWeight(),
                mgd.total.amount));
            buf.append("+----------------------------------------+----------+----------+------+\n");

            for (MaterialRecord record : mgd.records) {
                buf.append(String.format("|%40s|%10.3f|%10.3f|%6d|\n",
                        record.size,
                        record.getVolume(),
                        record.getWeight(),
                        record.amount));
            }
        }
        buf.append("+----------------------------------------+----------+----------+------+\n");
        return buf.toString();
    }
}
