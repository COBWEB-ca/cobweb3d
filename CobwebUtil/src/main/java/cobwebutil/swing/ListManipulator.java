package cobwebutil.swing;

import javax.swing.*;
import java.util.List;

public class ListManipulator<T> extends AbstractListModel<T> {
    private static final long serialVersionUID = 6521578944695127260L;

    List<T> items;

    public ListManipulator(List<T> list) {
        items = list;
    }

    public void addItem(T item) {
        items.add(item);
        fireIntervalAdded(this, items.size() - 1, items.size() - 1);
    }

    @Override
    public T getElementAt(int index) {
        return items.get(index);
    }

    @Override
    public int getSize() {
        return items.size();
    }

    public T removeItem(T item) {
        int index = items.indexOf(item);
        removeAt(index);
        return item;
    }

    public T removeAt(int index) {
        fireIntervalRemoved(this, index, index);
        return items.remove(index);
    }
}