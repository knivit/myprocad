package com.tsoft.myprocad.model;

import java.awt.geom.Area;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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

    public ItemList<AbstractMaterialItem> getMaterialItemsSubList() {
        ItemList<AbstractMaterialItem> items = new ItemList<>();
        items.addAll(getWallsSubList());
        items.addAll(getBeamsSubList());
        return items;
    }

    public static List<Pattern> getPatterns(ItemList<AbstractMaterialItem> items) {
        return items.stream().map(AbstractMaterialItem::getPattern).distinct().collect(Collectors.toList());
    }

    public static Set<Material> getMaterials(ItemList<AbstractMaterialItem> items) {
        HashSet<Material> materials = new HashSet<>();
        for (AbstractMaterialItem item : items) materials.add(item.getMaterial());
        return materials;
    }

    public static List<String> getMaterialsNames(ItemList<AbstractMaterialItem> items) {
        List<String> names = new ArrayList<>();
        for (Material material : getMaterials(items)) names.add(material.getName());
        return names;
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
        ItemList<T> items = new ItemList<>();
        for (T item : this) {
            if (item.isAtZLevel(level)) items.add(item);
        }
        return items;
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

    public ItemList<AbstractMaterialItem> filterByMaterials(MaterialList materials) {
        Set<AbstractMaterialItem> set = new HashSet<>();
        for (Material material : materials) {
            set.addAll(filterByMaterial(material));
        }
        return new ItemList<>(set);
    }

    public ItemList<AbstractMaterialItem> filterByMaterial(Material material) {
        ItemList<AbstractMaterialItem> list = new ItemList<>();
        for (AbstractMaterialItem item : (ItemList<AbstractMaterialItem>)this) {
            if (item.getMaterial().equals(material)) list.add(item);
        }
        return list;
    }

    public ItemList<AbstractMaterialItem> filterByPattern(Pattern pattern) {
        ItemList<AbstractMaterialItem> list = new ItemList<>();
        for (AbstractMaterialItem item : (ItemList<AbstractMaterialItem>)this) {
            if (item.getPattern().equals(pattern)) list.add(item);
        }
        return list;
    }
}
