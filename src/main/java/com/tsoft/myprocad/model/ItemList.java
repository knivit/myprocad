package com.tsoft.myprocad.model;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ItemList<T extends Item> extends ArrayList<T> {
    public ItemList() {
        super();
    }

    public ItemList(int initialCapacity) {
        super(initialCapacity);
    }

    public ItemList(Collection<T> source) {
        super(source);
    }

    public ItemList(T... items) {
        super();
        if (items != null) {
            for (T item : items) {
                add(item);
            }
        }
    }

    public ItemList<T> getDeepClone() {
        ItemList<T> list = new ItemList<>(size());
        for (Item item : this) {
            T clone = (T)item.clone();
            list.add(clone);
        }
        return list;
    }

    public ItemList<T> getCopy() {
        ItemList<T> list = new ItemList<>(size());
        list.addAll(this);
        return list;
    }

    public <S extends Item> ItemList getSubList(Class<S> subListClass) {
        List<S> list = stream().filter(item -> subListClass.isInstance(item)).map(item -> (S) item).collect(Collectors.toList());
        return new ItemList<>(list);
    }

    public T findById(String id) {
        Optional<T> existingItem = stream().filter(e -> e.getId().equals(id)).findFirst();
        return existingItem.isPresent() ? existingItem.get() : null;
    }

    public boolean undoItem(T item) {
        Item existingItem = findById(item.getId());
        if (existingItem == null) return false;

        int index = indexOf(existingItem);
        if (index != -1) {
            item.plan = get(index).plan;
            existingItem.populateFrom(item);
            return true;
        }
        return false;
    }

    public void deleteItem(T item) {
        int index = indexOf(item);
        if (index != -1) remove(index);
    }

    public ItemList<Wall> getWallsSubList() {
        return getSubList(Wall.class);
    }

    public ItemList<Beam> getBeamsSubList() {
        return getSubList(Beam.class);
    }

    public ItemList<DimensionLine> getDimensionLinesSubList() {
        return getSubList(DimensionLine.class);
    }

    public ItemList<LevelMark> getLevelMarksSubList() { return getSubList(LevelMark.class); }

    public ItemList<Label> getLabelsSubList() {
        return getSubList(Label.class);
    }

    public int getXMin() {
        Optional<T> min = stream().min(Comparator.comparingInt(Item::getXStart));
        return min.isPresent() ? min.get().getXStart() : 0;
    }

    public int getXMax() {
        Optional<T> max = stream().max(Comparator.comparingInt(Item::getXEnd));
        return max.isPresent() ? max.get().getXEnd() : 0;
    }

    public int getYMin() {
        Optional<T> min = stream().min(Comparator.comparingInt(Item::getYStart));
        return min.isPresent() ? min.get().getYStart() : 0;
    }

    public int getYMax() {
        Optional<T> max = stream().max(Comparator.comparingInt(Item::getYEnd));
        return max.isPresent() ? max.get().getYEnd() : 0;
    }

    public int getZMin() {
        Optional<T> min = stream().min(Comparator.comparingInt(Item::getZStart));
        return min.isPresent() ? min.get().getZStart() : 0;
    }

    public int getZMax() {
        Optional<T> max = stream().max(Comparator.comparingInt(Item::getZEnd));
        return max.isPresent() ? max.get().getZEnd() : 0;
    }

    public void sortByZLevel(final boolean desc) {
        Collections.sort(this, (o1, o2) -> {
            int result = (o1.getZEnd() < o2.getZEnd() ? 1 : (o1.getZEnd() > o2.getZEnd() ? -1 : 0));
            return (desc ? -result : result);
        });
    }

    public void resetItemCaches() {
        forEach(T::resetCaches);
    }

    public ItemList<T> atLevel(Plan plan) {
        return atLevel(plan.getLevel());
    }

    public ItemList<T> atLevel(Level level) {
        return stream().filter(item -> item.isAtZLevel(level)).collect(Collectors.toCollection(ItemList<T>::new));
    }

    /**
     * Returns an area matching the union of all <code>items</code> shapes.
     */
    public Area getItemsArea() {
        Area itemsArea = new Area();
        for (Item item : this) {
            itemsArea.add(new Area(item.getShape()));
        }
        return itemsArea;
    }

    public void forEach(Plan plan, Consumer<? super Item> action) {
        plan.getController().startBatchUpdate();
        try {
            forEach(action);
        } finally {
            plan.getController().stopBatchUpdate();
        }
    }

    public boolean isSame(ItemList otherList) {
        if (otherList == null || otherList.size() != size()) return false;
        return containsAll(otherList);
    }

    public void copyToClipboard() {
        ClipboardItems clipboardItems = new ClipboardItems();
        clipboardItems.copyToClipboard(this);
    }
}
