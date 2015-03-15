package com.tsoft.myprocad.report;

import com.tsoft.myprocad.model.Wall;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MaterialData {
    public MaterialRecord grandTotal;
    public List<MaterialGroupData> materialGroups;

    public MaterialData(Collection<Wall> walls) {
        calcMaterialData(walls);
        Collections.sort(materialGroups);
    }

    private void calcMaterialData(Collection<Wall> walls) {
        grandTotal = new MaterialRecord("__GRAND_TOTAL__"); // don't localize
        materialGroups = new ArrayList<>();

        for (Wall wall : walls) {
            if (wall.isSkipInReports()) continue;

            MaterialGroupData mgd = new MaterialGroupData(wall.getMaterial());
            int index = materialGroups.indexOf(mgd);
            if (index == -1) materialGroups.add(mgd);
            else mgd = materialGroups.get(index);

            grandTotal.add(wall.getWeight(), wall.getVolume(), wall.getPrice());
            grandTotal.amount ++;

            mgd.total.add(wall.getWeight(), wall.getVolume(), wall.getPrice());
            mgd.total.amount ++;

            String size = wall.getSize();
            MaterialRecord record = new MaterialRecord(size);
            int sizeIndex = mgd.records.indexOf(record);
            if (sizeIndex == -1) mgd.records.add(record);
            else record = mgd.records.get(sizeIndex);

            record.add(wall.getWeight(), wall.getVolume(), wall.getPrice());
            record.amount ++;
        }
    }
}