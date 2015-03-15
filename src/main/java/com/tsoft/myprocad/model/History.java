package com.tsoft.myprocad.model;

import java.util.stream.Collectors;

public class History {
    public abstract class Operation {
        public abstract void undo(Plan plan);
    }

    public class ItemOperation extends Operation {
        public Item item;

        public ItemOperation(Item item) {
            this.item = item;
        }

        @Override
        public void undo(Plan plan) {
            plan.undoItem(item);
        }
    }

    public class ItemListOperation extends Operation {
        public ItemList<Item> items;
        public CollectionEvent.Type event;

        public ItemListOperation(CollectionEvent.Type event, ItemList<Item> items) {
            this.event = event;
            this.items = items;
        }

        @Override
        public void undo(Plan plan) {
            // i.e. items properties change, rotate etc
            if (event == null) items.forEach(plan, plan::undoItem);
            else switch (event) {
                case ADD: { plan.undoAddItems(items); break; }
                case DELETE: { plan.undoDeleteItems(items); break; }
                default: throw new IllegalArgumentException("Unknown event " + event.name());
            }
        }
    }

    private class LimitedStack<V> {
        private int maxSize;
        private Object[] values;
        private int head;

        LimitedStack(int maxSize) {
            this.maxSize = maxSize;
            values = new Object[maxSize];
        }

        void push(V value) {
            if (head == maxSize) {
                for (int i = 1; i < maxSize; i ++) values[i - 1] = values[i];
            } else head ++;
            values[head - 1] = value;
        }

        V pop() {
            if (head == 0) return null;
            head --;

            V value = (V)values[head];
            values[head] = null;
            return value;
        }
    }

    public LimitedStack<Operation> operations;

    public History(int maxSize) {
        operations = new LimitedStack<>(maxSize);
    }

    public void push(Item item) {
        ItemOperation iop = new ItemOperation(item);
        operations.push(iop);
    }

    /** Changing state of a list of items, didn't change the list itself */
    public void push(ItemList<Item> items) {
        ItemListOperation op = new ItemListOperation(null, items);
        operations.push(op);
    }

    public void cloneAndPush(Item item) {
        push(item.historyClone());
    }

    public void cloneAndPush(ItemList<Item> items) {
        push(getClones(items));
    }

    /** Changing the list of items (adding or deleting items), didn't change items in the list */
    public void push(CollectionEvent.Type event, ItemList<Item> items) {
        ItemListOperation op = new ItemListOperation(event, getClones(items));
        operations.push(op);
    }

    public void push(CollectionEvent.Type event, Item item) {
        push(event, new ItemList<Item>(item));
    }

    public Operation pop() {
        return operations.pop();
    }

    private ItemList<Item> getClones(ItemList<Item> list) {
        return list.stream().map(Item::historyClone).collect(Collectors.toCollection(ItemList::new));
    }
}
