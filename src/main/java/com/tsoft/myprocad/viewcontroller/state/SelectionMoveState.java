package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.util.Cursors;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.util.stream.Collectors;
import javax.swing.*;
import java.util.Collections;

/**
 * Move selection state. This state manages the move of current selected items
 * with mouse and the selection of one item, if mouse isn't moved while button
 * is depressed. If duplication is activated during the move of the mouse,
 * moved items are duplicated first.
 */
public class SelectionMoveState extends ControllerState {
    private float xLastMouseMove;
    private float yLastMouseMove;
    private boolean mouseMoved;

    private ItemList<Item> oldSelection;
    private ItemList<Item> movedItems;
    private ItemList<Item> historyItems;

    public SelectionMoveState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.SELECTION;
    }

    @Override
    public void enter() {
        xLastMouseMove = getXLastMousePress();
        yLastMouseMove = getYLastMousePress();
        mouseMoved = false;

        ItemList<Item> itemsUnderCursor = getSelectableItemsAt(getXLastMousePress(), getYLastMousePress());
        oldSelection = planController.getSelectedItems();

        // If no selectable item under the cursor belongs to selection
        if (!itemsUnderCursor.isEmpty()) {
            if (Collections.disjoint(itemsUnderCursor, oldSelection)) {
                if (itemsUnderCursor.size() == 1) {
                    selectItem(itemsUnderCursor.get(0));
                } else {
                    popupSelectItemMenu(itemsUnderCursor);
                }
            }
        }

        movedItems = planController.getSelectedItems();

        historyItems = new ItemList<>();
        historyItems.addAll(movedItems.stream().map(Item::historyClone).collect(Collectors.toList()));

        planPanel.setCursor(Cursors.MOVE);
    }

    class PopupMenuItem extends JMenuItem {
        public Item item;
        public PopupMenuItem(Item item) {
            super(item.getPopupItemName());
            this.item = item;
        }
    }

    private void popupSelectItemMenu(ItemList<Item> items) {
        // show popup menu to select the one
        popupSelectItemMenu(items, e -> {
            PopupMenuItem menuItem = (PopupMenuItem) e.getSource();
            selectItem(menuItem.item);
        });
    }

    private void popupSelectItemMenu(ItemList<Item> items, ActionListener actionListener) {
        JPopupMenu selectItemPopupMenu = new JPopupMenu();
        for (Item item : items) {
            PopupMenuItem menuItem = new PopupMenuItem(item);
            menuItem.addActionListener(actionListener);
            menuItem.addChangeListener(e -> {
                PopupMenuItem mi = (PopupMenuItem)e.getSource();
                selectItem(mi.item);
            });
            selectItemPopupMenu.add(menuItem);
        }

        Point point = MouseInfo.getPointerInfo().getLocation();
        Point panelPoint = planPanel.getLocationOnScreen();
        point.translate(-panelPoint.x, -panelPoint.y);
        selectItemPopupMenu.show(planPanel, point.x, point.y);
    }

    @Override
    public void moveMouse(float x, float y) {
        movedItems.forEach(plan, e -> e.move(x - xLastMouseMove, y - yLastMouseMove, 0));

        if (!mouseMoved) {
            selectItems(movedItems);
        }
        planPanel.makePointVisible(x, y);
        xLastMouseMove = x;
        yLastMouseMove = y;
        mouseMoved = true;

        planController.selectionStateChanged();
    }

    @Override
    public void releaseMouse(float x, float y) {
        if (mouseMoved) {
            history.push(historyItems);
        } else {
            // If mouse didn't move, select only the item at (x,y)
            ItemList<Item> itemsUnderCursor = getSelectableItemsAt(x, y);
            if (!itemsUnderCursor.isEmpty()) {
                if (itemsUnderCursor.size() == 1) {
                    selectItem(itemsUnderCursor.get(0));
                } else {
                    popupSelectItemMenu(itemsUnderCursor);
                }
            }
        }

        // Change the state to SelectionState
        setState(getSelectionState());
    }

    @Override
    public void escape() {
        if (mouseMoved) {
            float dx = getXLastMousePress() - xLastMouseMove;
            float dy = getYLastMousePress() - yLastMouseMove;
            movedItems.forEach(plan, e -> e.move(dx, dy, 0));
        }

        // Change the state to SelectionState
        setState(getSelectionState());
    }

    @Override
    public void exit() {
        movedItems = null;
        planPanel.deleteFeedback();
    }
}
