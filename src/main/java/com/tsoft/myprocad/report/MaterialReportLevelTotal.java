package com.tsoft.myprocad.report;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Plan;

public class MaterialReportLevelTotal extends MaterialReport {
    @Override
    public String getLocalizedName() {
        return L10.get(L10.MATERIAL_REPORT_LEVEL_TOTAL);
    }

    @Override
    public String generate(MaterialReportParams params) {
        Plan plan = params.plan;

        MaterialData materialData = new MaterialData(plan.getLevelWalls());

        StringBuilder buf = new StringBuilder();
        buf.append(plan.getName()).append('\n');;
        buf.append("+----------------------------------------+----------+----------+----------+----------+----------+\n");
        buf.append(String.format("|%40s|%10s|%10s|%10s|%10s|%10s|\n",
                L10.get(L10.MATERIAL_REPORT_MATERIAL_COLUMN),
                L10.get(L10.MATERIAL_REPORT_AMOUNT_COLUMN),
                L10.get(L10.MATERIAL_REPORT_VOLUME_COLUMN),
                L10.get(L10.MATERIAL_REPORT_WEIGHT_COLUMN),
                L10.get(L10.MATERIAL_REPORT_PRICE_COLUMN),
                L10.get(L10.MATERIAL_REPORT_COST_COLUMN)));
        buf.append("+----------------------------------------+----------+----------+----------+----------+----------+\n");
        for (MaterialGroupData mgd : materialData.materialGroups) {
            buf.append(String.format("|%-40s|%10d|%10.3f|%10.3f|%10.2f|%10d|\n",
                mgd.material.getName(),
                mgd.total.amount,
                mgd.total.getVolume(),
                mgd.total.getWeight(),
                mgd.material.getPrice(),
                mgd.total.getPrice()));
        }
        buf.append("+----------------------------------------+----------+----------+----------+----------+----------+\n");
        buf.append(String.format("|%-40s|          |%10.3f|%10.3f|          |%10d|\n",
            L10.get(L10.MATERIAL_REPORT_GRAND_TOTAL),
            materialData.grandTotal.getVolume(),
            materialData.grandTotal.getWeight(),
            materialData.grandTotal.getPrice()));
        buf.append("+----------------------------------------+----------+----------+----------+----------+----------+\n");
        return buf.toString();
    }
}