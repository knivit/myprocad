package com.tsoft.myprocad.model;

import java.util.EventObject;

/**
 * Type of event notified when an item is added or deleted from a list.
 * <code>T</code> is the type of item stored in the collection.
 */
public class CollectionEvent<T> extends EventObject {
    /**
     * The type of change in the collection.
     */
    public enum Type { ADD, DELETE }

    private final T item;
    private final int index;
    private final Type type;

    /**
     * Creates an event for an item that has no index.
     */
    public CollectionEvent(Object source, T item, Type type) {
        this(source, item, -1, type);
    }

    /**
     * Creates an event for an item with its index.
     */
    public CollectionEvent(Object source, T item, int index, Type type) {
        super(source);
        this.item = item;
        this.index = index;
        this.type =  type;
    }

    /**
     * Returns the added or deleted item.
     */
    public T getItem() {
        return this.item;
    }

    /**
     * Returns the index of the item in collection or -1 if this index is unknown.
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * Returns the type of event.
     */
    public Type getType() {
        return this.type;
    }
}
