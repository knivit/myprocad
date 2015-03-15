package com.tsoft.myprocad.viewcontroller.component;

import java.util.List;

public abstract class AbstractListController {
    protected boolean moveElementUp(List elements, Object element) {
        int index = elements.indexOf(element);
        if (index > 0) {
            elements.remove(index);
            elements.add(index - 1, element);
            return true;
        }
        return false;
    }

    protected boolean moveElementDown(List elements, Object element) {
        int index = elements.indexOf(element);
        if (index >= 0 && index < (elements.size() - 1)) {
            elements.remove(index);
            elements.add(index + 1, element);
            return true;
        }
        return false;
    }
}
