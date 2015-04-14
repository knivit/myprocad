package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.viewcontroller.MaterialsTableModel;

import java.util.ArrayList;
import java.util.Optional;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

public class MaterialList extends ArrayList<Material> implements TableDialogPanelSupport<Material> {
    private Project project;
    private MaterialsTableModel tableModel;

    public MaterialList() {
        super();
    }

    public MaterialList(ItemList<AbstractMaterialItem> materialItems) {
        super();
        for (AbstractMaterialItem mi : materialItems) add(mi.getMaterial());
    }

    public void setProject(Project project) { this.project = project; }

    public Material findByName(String name) {
        Optional<Material> optional = stream().filter(e -> e.equalByName(name)).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }

    @Override
    public TableDialogPanelSupport<Material> getDeepClone() {
        MaterialList copyList = new MaterialList();
        for (Material material : this) {
            Material copy = material.clone();
            copyList.add(copy);
        }
        return copyList;
    }

    @Override
    public AbstractTableModel getTableModel() {
        if (tableModel == null) tableModel = new MaterialsTableModel(this);
        return tableModel;
    }

    @Override
    public void setupCustomColumns(JTable table) {
        JComboBox<MaterialUnit> comboBox = new JComboBox<>();
        for (MaterialUnit unit : MaterialUnit.values()) {
            comboBox.addItem(unit);
        }
        TableColumn unitColumn = table.getColumnModel().getColumn(3);
        unitColumn.setCellEditor(new DefaultCellEditor(comboBox));
    }

    @Override
    public Material addDialog() {
        Material material = new Material();
        project.addMaterial(material);
        return material;
    }

    @Override
    public boolean deleteDialog(Material material) {
        if (SwingTools.showConfirmDialog(L10.get(L10.CONFIRM_MATERIAL_REMOVAL, material.getName()))) {
            // don't remove the default material
            if (material.isDefault()) {
                SwingTools.showMessage(L10.get(L10.CANT_REMOVE_DEFAULT_MATERIAL, material.getName()));
                return false;
            }

            // don't remove used materials
            StringBuilder buf = new StringBuilder();
            for (Plan plan : project.getAllPlans()) {
                ItemList<AbstractMaterialItem> materialItems = plan.getMaterialItems().filterByMaterial(material);
                if (!materialItems.isEmpty()) {
                    if (buf.length() > 0) buf.append(", ");
                    buf.append(plan.getName());
                }
            }

            if (buf.length() > 0) {
                SwingTools.showMessage(L10.get(L10.CANT_REMOVE_MATERIAL, material.getName(), buf.toString()));
                return false;
            }

            return remove(material);
        }
        return false;
    }

    public Material findById(long id) {
        for (Material material : this)
            if (material.getId() == id) return material;
        return null;
    }

    public Material getDefault() {
        Optional<Material> optional = stream().filter(Material::isDefault).findFirst();
        return optional.isPresent() ? optional.get() : null;
    }
}
