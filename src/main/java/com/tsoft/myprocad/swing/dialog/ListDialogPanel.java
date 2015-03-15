package com.tsoft.myprocad.swing.dialog;

import com.tsoft.myprocad.l10n.L10;

import java.awt.*;
import java.util.List;
import java.util.function.Function;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/** A panel with a list and controls to add/delete items */
public class ListDialogPanel<E> extends AbstractDialogPanel {
    private JList<E> list;

    public ListDialogPanel(List<E> elements,
                           Function<E, Boolean> moveUpFunc,
                           Function<E, Boolean> moveDownFunc,
                           Function<Integer, E> addFunc,
                           Function<E, Boolean> deleteFunc) {
        this(elements, moveUpFunc, moveDownFunc, addFunc, deleteFunc, null);
    }

    public ListDialogPanel(List<E> elements,
                           Function<E, Boolean> moveUpFunc,
                           Function<E, Boolean> moveDownFunc,
                           Function<Integer, E> addFunc,
                           Function<E, Boolean> deleteFunc,
                           Function<E, Boolean> renameFunc) {
        super(new BorderLayout());

        list = new JList<>();
        createListModel(elements);
        list.setVisibleRowCount(-1);
        JScrollPane scrollPane = new JScrollPane(list);

        JPanel toolbar = new JPanel(new GridLayout(0, 1));

        JButton moveUp = new JButton(L10.get(L10.MOVE_UP_BUTTON));
        moveUp.addActionListener(e -> {
            if (moveUpFunc.apply(list.getSelectedValue())) createListModel(elements);
        });
        toolbar.add(moveUp);

        JButton moveDown = new JButton(L10.get(L10.MOVE_DOWN_BUTTON));
        moveDown.addActionListener(e -> {
            if (moveDownFunc.apply(list.getSelectedValue())) createListModel(elements);
        });
        toolbar.add(moveDown);

        JButton add = new JButton(L10.get(L10.ADD_BUTTON));
        add.addActionListener(e -> {
            int index = list.getSelectedIndex();
            E newElement = addFunc.apply(index < 0 ? elements.size() : index);
            if (newElement != null) createListModel(elements);
        });
        toolbar.add(add);

        JButton delete = new JButton(L10.get(L10.DELETE_BUTTON));
        delete.addActionListener(e -> {
            if (list.getSelectedValue() != null && deleteFunc.apply(list.getSelectedValue())) createListModel(elements);
        });
        toolbar.add(delete);

        if (renameFunc != null) {
            JButton rename = new JButton(L10.get(L10.RENAME_BUTTON));
            rename.addActionListener(e -> {
                if (list.getSelectedValue() != null && renameFunc.apply(list.getSelectedValue()))
                    createListModel(elements);
            });
            toolbar.add(rename);
        }

        toolbar.add(Box.createVerticalGlue());
        JPanel toolBarPanel = new JPanel(new BorderLayout());
        toolBarPanel.add(toolbar, BorderLayout.NORTH);

        add(BorderLayout.CENTER, scrollPane);
        add(BorderLayout.EAST, toolBarPanel);
    }

    @Override
    public Dimension getDialogPreferredSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int)Math.round(screenSize.width * 0.4), (int)Math.round(screenSize.height * 0.6));
    }

    private void createListModel(List<E> elements) {
        E selected = list.getSelectedValue();
        DefaultListModel<E> listModel = new DefaultListModel<>();
        elements.forEach(listModel::addElement);

        list.setModel(listModel);
        list.setSelectedValue(selected, true);
    }
}
