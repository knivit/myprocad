package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.viewcontroller.PlanController;

import com.tsoft.myprocad.viewcontroller.PlanController.Mode;
import java.util.stream.Collectors;

/**
 * Selection with rectangle state. This state manages selection when mouse
 * press is done outside of an item or when mouse press is done with shift key
 * down.
 */
public class RectangleSelectionState extends ControllerState {
    private ItemList<Item> selectedItemsMousePressed;
    private boolean mouseMoved;

    public RectangleSelectionState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.SELECTION;
    }

    @Override
    public void enter() {
        ItemList<Item> itemsUnderCursor = getSelectableItemsAt(getXLastMousePress(), getYLastMousePress());

        // If no item under cursor and shift wasn't down, deselect all
        if (itemsUnderCursor.isEmpty() && !wasShiftDownLastMousePress()) {
            deselectAll();
        }

        // Store current selection
        selectedItemsMousePressed = planController.getSelectedItems();
        mouseMoved = false;
    }

    @Override
    public void moveMouse(float x, float y) {
        mouseMoved = true;
        updateSelectedItems(getXLastMousePress(), getYLastMousePress(), x, y, selectedItemsMousePressed);

        // Update rectangle feedback
        planPanel.setRectangleFeedback(getXLastMousePress(), getYLastMousePress(), x, y);
        planPanel.makePointVisible(x, y);
    }

    @Override
    public void releaseMouse(float x, float y) {
        // If cursor didn't move
        if (!mouseMoved) {
            ItemList<Item> itemsUnderCursor = getSelectableItemsAt(x, y);

            // Toggle selection of the item under cursor
            if (!itemsUnderCursor.isEmpty()) {
                Item item = itemsUnderCursor.get(0);
                if (selectedItemsMousePressed.contains(item)) {
                    selectedItemsMousePressed.remove(item);
                } else {
                    selectedItemsMousePressed.add(item);
                }
                selectItems(selectedItemsMousePressed);
            }
        }

        // Change state to SelectionState
        setState(getSelectionState());
    }

    @Override
    public void escape() {
        setState(getSelectionState());
    }

    @Override
    public void exit() {
        selectedItemsMousePressed = null;
        planPanel.deleteFeedback();
    }

    /**
     * Updates selection from <code>selectedItemsMousePressed</code> and the
     * items that intersects the rectangle at coordinates (<code>x0</code>,
     * <code>y0</code>) and (<code>x1</code>, <code>y1</code>).
     */
    private void updateSelectedItems(float x0, float y0, float x1, float y1, ItemList<Item> selectedItemsMousePressed) {
        ItemList<Item> selectedItems = new ItemList<>();
        boolean shiftDown = wasShiftDownLastMousePress();
        if (shiftDown) {
            selectedItems.addAll(selectedItemsMousePressed);
        }

        // For all the items that intersect with rectangle
        for (Item item : getSelectableItemsIntersectingRectangle(x0, y0, x1, y1)) {
            // If shift was down at mouse press
            if (shiftDown) {
                // Toggle selection of item
                if (selectedItemsMousePressed.contains(item)) {
                    selectedItems.remove(item);
                } else {
                    selectedItems.add(item);
                }
            } else if (!selectedItemsMousePressed.contains(item)) {
                // Else select the wall
                selectedItems.add(item);
            }
        }

        // Update selection
        selectItems(selectedItems);
    }

    /**
     * Returns the items that intersects with the rectangle of (<code>x0</code>,
     * <code>y0</code>), (<code>x1</code>, <code>y1</code>) opposite corners.
     */
    private ItemList<Item> getSelectableItemsIntersectingRectangle(float x0, float y0, float x1, float y1) {
        return planPanel.getPaintedItems().stream().filter(e -> e.intersectsRectangle(plan, x0, y0, x1, y1)).
            collect(Collectors.toCollection(ItemList::new));
    }
}
