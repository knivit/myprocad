package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Selection {
    private ItemList<Item> items = new ItemList<>();

    public Selection(ItemList<Item> items) {
        this.items = items;
    }

    public ItemList<Item> getItems() {
        return items;
    }

    public int getXMin() {
        return items == null ? 0 : items.getXMin();
    }

    public int getXMax() {
        return items == null ? 0 : items.getXMax();
    }

    public String validateMoveX(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);

        Optional<? extends Item> min = items.stream().min(Comparator.comparingInt(Item::getXStart));
        if (min.isPresent()) {
            int x = value - min.get().getXStart();
            if (x < Item.MIN_COORDINATE || x > Item.MAX_COORDINATE)
                return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
        }

        Optional<? extends Item> max = items.stream().max(Comparator.comparingInt(Item::getXEnd));
        if (max.isPresent()) {
            int x = value - max.get().getXEnd();
            if (x < Item.MIN_COORDINATE || x > Item.MAX_COORDINATE)
                return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
        }
        return null;
    }

    public void moveX(Plan plan, int x) {
        if (items == null) return;

        Optional<? extends Item> min = items.stream().min(Comparator.comparingInt(Item::getXStart));
        if (min.isPresent()) shiftX(plan, x - min.get().getXStart());
    }

    public String validateShift(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < Item.MIN_COORDINATE || value > Item.MAX_COORDINATE)
            return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
        return null;
    }

    public void shiftX(Plan plan, int dx) {
        if (items != null) {
            items.forEach(plan, e -> e.move(dx, 0, 0));
        }
    }

    public int getYMin() {
        return items == null ? 0 : items.getYMin();
    }

    public int getYMax() {
        return items == null ? 0 : items.getYMax();
    }

    public String validateMoveY(int value) {
        Optional<? extends Item> min = items.stream().min(Comparator.comparingInt(Item::getYStart));
        if (min.isPresent()) {
            int y = value - min.get().getYStart();
            if (y < Item.MIN_COORDINATE || y > Item.MAX_COORDINATE)
                return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
        }

        Optional<? extends Item> max = items.stream().max(Comparator.comparingInt(Item::getYEnd));
        if (max.isPresent()) {
            int y = value - max.get().getYEnd();
            if (y < Item.MIN_COORDINATE || y > Item.MAX_COORDINATE)
                return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
        }
        return null;
    }

    public void moveY(Plan plan, int y) {
        if (items == null) return;

        Optional<? extends Item> min = items.stream().min(Comparator.comparingInt(Item::getYStart));
        if (min.isPresent()) shiftY(plan, y - min.get().getYStart());
    }

    public void shiftY(Plan plan, int dy) {
        if (items != null) {
            items.forEach(plan, e -> e.move(0, dy, 0));
        }
    }

    public int getZMin() {
        return items == null ? 0 : items.getZMin();
    }

    public int getZMax() {
        return items == null ? 0 : items.getZMax();
    }

    public void setZStart(Plan plan, int z) {
        if (items != null) {
            items.forEach(plan, e -> e.setZStart(z));
        }
    }

    public void setZEnd(Plan plan, int z) {
        if (items != null) {
            items.forEach(plan, e -> e.setZEnd(z));
        }
    }

    public void moveZ(Plan plan, int z) {
        if (items == null) return;

        // find out an Item with minimal coordinate
        Optional<? extends Item> min = items.stream().min(Comparator.comparingInt(item -> item.getZStart()));
        if (min.isPresent()) moveDz(plan, z - min.get().getZStart());
    }

    public void moveDz(Plan plan, int dz) {
        if (items != null) {
            items.forEach(plan, e -> e.move(0, 0, dz));
        }
    }

    public int getWallsAmount() {
        return items == null ? 0 : items.getWallsSubList().size();
    }

    public float getWallsOuterLength() {
        if (items == null) return 0;

        ItemList<Wall> walls = items.getWallsSubList();
        if (walls.isEmpty()) return 0;

        int xMin = walls.getXMin();
        int xMax = walls.getXMax();
        if (xMax < xMin) { int tmp = xMax; xMax = xMin; xMin = tmp; }

        int yMin = walls.getYMin();
        int yMax = walls.getYMax();
        if (yMax < yMin) { int tmp = yMax; yMax = yMin; yMin = tmp; }

        int[] zxLeft = new int[xMax - xMin + 1];
        Arrays.fill(zxLeft, Integer.MAX_VALUE);
        int[] zxRight = new int[xMax - xMin + 1];
        Arrays.fill(zxRight, Integer.MIN_VALUE);

        int[] zyTop = new int[yMax - yMin + 1];
        Arrays.fill(zyTop, Integer.MAX_VALUE);
        int[] zyBottom = new int[yMax - yMin + 1];
        Arrays.fill(zyBottom, Integer.MIN_VALUE);

        for (Wall wall : walls) {
            for (int x = wall.getXStart(); x < wall.getXEnd(); x ++) {
                if (zxLeft[x - xMin] > wall.getYStart()) zxLeft[x - xMin] = wall.getYStart();
                if (zxRight[x - xMin] < wall.getYEnd()) zxRight[x - xMin] = wall.getYEnd();
            }
            for (int y = wall.getYStart(); y < wall.getYEnd(); y ++) {
                if (zyTop[y - yMin] > wall.getXStart()) zyTop[y - yMin] = wall.getXStart();
                if (zyBottom[y - yMin] < wall.getXEnd()) zyBottom[y - yMin] = wall.getXEnd();
            }
        }

        int length = 0;
        for (int i = 0; i < zxLeft.length; i ++) length += (zxLeft[i] == Integer.MAX_VALUE ? 0 : 1);
        for (int i = 0; i < zxRight.length; i ++) length += (zxRight[i] == Integer.MIN_VALUE ? 0 : 1);
        for (int i = 0; i < zyTop.length; i ++) length += (zyTop[i] == Integer.MAX_VALUE ? 0 : 1);
        for (int i = 0; i < zyBottom.length; i ++) length += (zyBottom[i] == Integer.MIN_VALUE ? 0 : 1);

        // millimeters to m
        return length / 1000f;
    }

    public double getMaterialItemsArea() {
        double area = 0;
        if (items != null) {
            area = items.getWallsSubList().stream().map(e -> e.getArea()).reduce(0.0, Double::sum);
            area += items.getBeamsSubList().stream().map(e -> e.getArea()).reduce(0.0, Double::sum);
        }
        return area;
    }

    public double getMaterialItemsVolume() {
        double volume = 0;
        if (items != null) {
            volume = items.getWallsSubList().stream().map(Wall::getVolume).reduce(0.0, Double::sum);
            volume += items.getBeamsSubList().stream().map(Beam::getVolume).reduce(0.0, Double::sum);
        }
        return volume;
    }

    public double getMaterialItemsWeight() {
        double weight = 0;
        if (items != null) {
            weight = items.getWallsSubList().stream().map(Wall::getWeight).reduce(0.0, Double::sum);
            weight += items.getBeamsSubList().stream().map(Beam::getWeight).reduce(0.0, Double::sum);
        }
        return weight;
    }

    public double getMaterialItemsPrice() {
        double price = 0;
        if (items != null) {
            price = items.getWallsSubList().stream().map(Wall::getPrice).reduce(0.0, Double::sum);
            price += items.getBeamsSubList().stream().map(Beam::getPrice).reduce(0.0, Double::sum);
        }
        return price;
    }

    public ItemList<DimensionLine> getDimensionLinesSubList() { return items.getDimensionLinesSubList(); }

    public void addItems(ItemList<Wall> newWalls) {
        items.addAll(newWalls);
    }

    public void removeItems(ItemList<Item> newWalls) {
        items.removeAll(newWalls);
    }
}
