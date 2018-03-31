package cobweb3d.ui.util.binding;

import cobweb3d.ui.config.PropertyAccessor;

public interface FieldBoundComponent {
    String getLabelText();

    static Object validate(PropertyAccessor field, Object value) {
        Object out = null;
        if (field.getType().equals(value.getClass())) {
            out = value;
        } else if (value instanceof Double) {
            Double d = (Double) value;
            if (field.getType().equals(double.class)) {
                out = d;
            } else if (field.getType().equals(float.class)) {
                out = d.floatValue();
            }
        } else if (value instanceof Long) {
            Long l = (Long) value;
            if (field.getType().equals(long.class)) {
                out = l;
            } else if (field.getType().equals(int.class)) {
                out = l.intValue();
            } else if (field.getType().equals(float.class)) {
                out = l.floatValue();
            }
        } else {
            throw new IllegalArgumentException("bad input/output combination: " + value.getClass() + " -> " + field.getType());
        }
        return out;
    }
}