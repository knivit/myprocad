package com.tsoft.myprocad.swing.dialog;

import com.l2fprod.common.swing.renderer.DefaultCellRenderer;
import com.tsoft.myprocad.util.StringUtil;

import javax.swing.*;
import java.util.List;

public class InputListElement<T> extends AbstractInputElement<T, JComboBox> {
    public List<T> selectList;
    private DefaultCellRenderer renderer;

    public InputListElement(String caption, List<T> selectList) {
        this(caption, selectList, null);
    }

    public InputListElement(String caption, List<T> selectList, String initialValue) {
        super(caption);
        this.selectList = selectList;
        this.initialValue = initialValue;
    }

    public void setRenderer(DefaultCellRenderer renderer) {
        this.renderer = renderer;
    }

    @Override
    protected JComboBox createComponent() {
        JComboBox jcb = new JComboBox(selectList.toArray());
        jcb.setEditable(false);
        if (!StringUtil.isEmpty(initialValue)) {
            int index = 0;
            initialValue = initialValue.toLowerCase();
            for (T value : selectList) {
                String strValue = value.toString();
                if (strValue.toLowerCase().startsWith(initialValue)) break;
                index ++;
            }
            jcb.setSelectedIndex(index);
        }

        if (renderer != null) jcb.setRenderer(renderer);
        return jcb;
    }

    @Override
    public T getValue() {
        return (T)component.getSelectedItem();
    }
}