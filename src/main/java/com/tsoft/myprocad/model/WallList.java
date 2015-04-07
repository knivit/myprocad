package com.tsoft.myprocad.model;

import java.util.*;
import java.util.stream.Collectors;

public class WallList extends ItemList<Wall> {
    public WallList() {
        super();
    }

    public WallList(Collection<Wall> source) {
        super(source);
    }

    public WallList filterByMaterials(MaterialList materials) {
        Set<Wall> set = new HashSet<>();
        for (Material material : materials) {
            set.addAll(filterByMaterial(material));
        }
        return new WallList(set);
    }

    public WallList filterByPattern(Pattern pattern) {
        return stream().filter(wall -> wall.getPattern().equals(pattern)).collect(Collectors.toCollection(WallList::new));
    }

    public WallList filterByMaterial(Material material) {
        List<Wall> list = stream().filter(e -> e.getMaterial().equals(material)).collect(Collectors.toList());
        return new WallList(list);
    }
}
