package cobweb3d.ui.config;

import cobwebutil.io.ConfDisplayFormat;

import java.lang.reflect.Array;

/**
 * PropertyAccessor that gets/sets an array element.
 */
public class ArrayPropertyAccessor extends PropertyAccessorBase {
    private final int index;

    private final String format;

    /**
     * Creates accessor for array element i of array returned by parent accessor.
     *
     * @param parent parent accessor that returns an array
     * @param i      array index
     */
    public ArrayPropertyAccessor(PropertyAccessor parent, int i) {
        super(parent);
        if (!parent.getType().isArray())
            throw new IllegalArgumentException("Parent must return an array!");

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
        if (obj instanceof ArrayPropertyAccessor) {
            ArrayPropertyAccessor o = (ArrayPropertyAccessor) obj;
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
        Object value = Array.get(object, index);
        return value;
    }

    @Override
    public void thisSetValue(Object object, Object value) {
        try {
            Array.set(object, index, value);
        } catch (IllegalArgumentException ex) {
            return;
            //throw new UserInputException("Invalid Value");
        }
    }

    @Override
    public Class<?> getType() {
        return parent.getType().getComponentType();
    }

    @Override
    protected String thisToString() {
        return "[" + index + "]";
    }
}
