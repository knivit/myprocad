package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.Wall;
import com.tsoft.myprocad.util.SwingTools;

public enum PasteOperation {
    // first item is placed to the given location (pasteX, pasteY, pasteZ)
    // and the offsets are calculated
    // others are moved on these offsets
    MOVE_TO(1) {
        @Override
        public void paste(Plan plan, ItemList<Item> items) {
            items.forEach(plan::addItem);
        }

        @Override
        public String toString() {
            return L10.get(L10.PASTE_OPERATION_MOVE_TO);
        }
    },

    // find out Z min along the selection
    // and move it on the level
    MOVE_TO_LEVEL(2) {
        @Override
        public void paste(Plan plan, ItemList<Item> items) {
            items.forEach(plan::addItem);
            plan.moveItemsToLevel(plan.getLevel(), items);
        }

        @Override
        public String toString() {
            return L10.get(L10.PASTE_OPERATION_MOVE_TO_LEVEL);
        }
    },

    PLUS_OFFSET(3) {
        @Override
        public void paste(Plan plan, ItemList<Item> items) {
            items.forEach(plan, item -> {
                plan.addItem(item);
                item.move(plan.getPasteOffsetX(), plan.getPasteOffsetY(), plan.getPasteOffsetZ());
            });
        }

        @Override
        public String toString() {
            return L10.get(L10.PASTE_OPERATION_PLUS_OFFSET);
        }
    },

    FIRST_SELECTED_PLUS_OFFSET(4) {
        @Override
        public void paste(Plan plan, ItemList<Item> items) {
            ItemList<Item> selectedItems = plan.getController().getSelectedItems();
            Item selectedAnchor = getAnchor(selectedItems);
            Item copiedAnchor = getAnchor(items);
            if (selectedAnchor == null) { SwingTools.showMessage(L10.get(L10.SELECT_ITEM)); return; }

            int offx = Math.abs(selectedAnchor.getXStart() - copiedAnchor.getXStart());
            int offy = Math.abs(selectedAnchor.getYStart() - copiedAnchor.getYStart());
            int offz = Math.abs(selectedAnchor.getZStart() - copiedAnchor.getZStart());
            int dx = plan.getPasteOffsetX() + (plan.getPasteOffsetX() < 0 ? -offx : offx);
            int dy = plan.getPasteOffsetY() + (plan.getPasteOffsetY() < 0 ? -offy : offy);
            int dz = plan.getPasteOffsetZ() + (plan.getPasteOffsetZ() < 0 ? -offz : offz);

            items.forEach(plan, item -> {
                plan.addItem(item);
                item.move(dx, dy, dz);
            });
        }

        @Override
        public String toString() {
            return L10.get(L10.PASTE_OPERATION_FIRST_SELECTED_PLUS_OFFSET);
        }
    };

    private int id;

    public abstract void paste(Plan plan, ItemList<Item> items);

    PasteOperation(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    /** Look for an Anchor for items to be pasted */
    private static Item getAnchor(ItemList<Item> items) {
        Item topLeftWall = null;
        Item topLeftItem = null;

        for (Item item : items) {
            // top-left Item
            if (topLeftItem == null) topLeftItem = item;
            else if (topLeftItem.getXStart() > item.getXStart()) topLeftItem = item;
            else if (topLeftItem.getYStart() > item.getYStart()) topLeftItem = item;

            // top-left Wall
            if (item instanceof Wall) {
                if (topLeftWall == null) topLeftWall = item;
                else if (topLeftWall.getXStart() > item.getXStart()) topLeftWall = item;
                else if (topLeftWall.getYStart() > item.getYStart()) topLeftWall = item;
            }
        }

        // if there is a wall in the selection, prefer it
        return topLeftWall == null ? topLeftItem : topLeftWall;
    }

    public static PasteOperation findById(int id) {
        for (PasteOperation op : values()) {
            if (op.getId() == id) return op;
        }
        return null;
    }
}
