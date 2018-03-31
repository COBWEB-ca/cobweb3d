package cobweb3d.ui.config;

import cobwebutil.io.ConfDisplayFormat;
import cobwebutil.io.ConfListType;

import java.util.List;

/**
 * PropertyAccessor that gets/sets a list element.
 */
public class ListPropertyAccessor extends PropertyAccessorBase {
    private final int index;

    private final String format;

    /**
     * Creates accessor for list element i of list returned by parent accessor.
     *
     * @param parent parent accessor that returns an list
     * @param i      list index
     */
    public ListPropertyAccessor(PropertyAccessor parent, int i) {
        super(parent);
        if (!List.class.isAssignableFrom(parent.getType()))
            throw new IllegalArgumentException("Parent must return a List!");

        this.index = i;
        ConfDisplayFormat dispFormat = getAnnotationSource().getAnnotation(ConfDisplayFormat.class);
        this.format = dispFormat != null ? dispFormat.value() : null;
    }

    @Override
    protected int thisHashCode() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ListPropertyAccessor) {
            ListPropertyAccessor o = (ListPropertyAccessor) obj;
            return super.equals(o) && index == o.index;
        }
        return false;
    }

    @Override
    public String getName() {
        if (format != null)
            return String.format(format, parent.getName(), index + 1);
        else
            return super.getName();
    }

    @Override
    protected String thisGetName() {
        return Integer.toString(index + 1);
    }

    @Override
    public Object thisGetValue(Object object) {
        @SuppressWarnings("unchecked")
        List<Object> list = (List<Object>) object;
        Object value = list.get(index);
        return value;
    }

    @Override
    public void thisSetValue(Object object, Object value) {
        try {
            @SuppressWarnings("unchecked")
            List<Object> list = (List<Object>) object;
            list.set(index, value);
        } catch (IllegalArgumentException ex) {
            return;
            //throw new UserInputException("Invalid Value");
        }
    }

    @Override
    public Class<?> getType() {
        return getAnnotationSource().getAnnotation(ConfListType.class).value();
    }

    @Override
    public String thisToString() {
        return ".get(" + index + ")";
    }
}
