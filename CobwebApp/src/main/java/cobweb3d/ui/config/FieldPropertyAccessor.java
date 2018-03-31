package cobweb3d.ui.config;

import cobwebutil.io.ConfDisplayName;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;

/**
 * PropertyAccessor that gets/sets object fields.
 */
public class FieldPropertyAccessor extends PropertyAccessorBase {
    protected Field field;

    /**
     * Creates accessor for object field
     *
     * @param f field to use
     */
    public FieldPropertyAccessor(Field f) {
        this(null, f);
    }

    /**
     * Creates a nested accessor, for example obj.a.b can be accessed with
     * new FieldPropertyAccessor(new FieldPropertyAccessor(aField), bField);
     *
     * @param parent parent accessor
     * @param f      field to use
     */
    public FieldPropertyAccessor(PropertyAccessor parent, Field f) {
        super(parent);
        field = f;
    }

    @Override
    protected int thisHashCode() {
        return field.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FieldPropertyAccessor) {
            FieldPropertyAccessor o = (FieldPropertyAccessor) obj;
            return super.equals(o) && field.equals(o.field);
        }
        return false;
    }

    @Override
    public String thisGetName() {
        ConfDisplayName nameAnnotation = getAnnotationSource().getAnnotation(ConfDisplayName.class);
        String name = nameAnnotation != null ? nameAnnotation.value() : field.getName();
        return name;
    }

    @Override
    public Object thisGetValue(Object object) {
        Object value = null;
        try {
            value = this.field.get(object);
        } catch (IllegalAccessException ex) {
            throw new RuntimeException("This field seems to be broken: " + this.toString(), ex);
        }
        return value;
    }

    @Override
    public void thisSetValue(Object object, Object value) {
        try {
            field.set(object, value);
        } catch (IllegalAccessException ex) {
            throw new IllegalArgumentException("Tagged field is not public: " + field.getName(), ex);
        } catch (IllegalArgumentException ex) {
            return;
            //throw new UserInputException("Invalid Value");
        }
    }

    @Override
    public Class<?> getType() {
        return field.getType();
    }

    @Override
    public String thisToString() {
        return "." + field.getName();
    }

    @Override
    public AnnotatedElement getAnnotationSource() {
        return field;
    }

}
