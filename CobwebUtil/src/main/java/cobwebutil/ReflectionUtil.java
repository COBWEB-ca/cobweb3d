package cobwebutil;

import java.lang.reflect.Field;

public class ReflectionUtil {

    /**
     * Modifies value of <code>object.field</code> using the formula <code>x' = x * m + b</code>
     *
     * @param object Object to be modified
     * @param field  Field to be modified
     * @param m      scale factor
     * @param b      offset factor
     */
    public static void modifyFieldLinear(Object object, Field field, float m, float b) {
        float currentValue = getFieldAsFloat(object, field);
        float newValue = currentValue * m + b;
        setFieldWithFloat(object, field, newValue);
    }

    public static float getFieldAsFloat(Object object, Field field) {
        try {
            if (field.getType().equals(float.class)) {
                return field.getFloat(object);
            } else if (field.getType().equals(int.class)) {
                return field.getInt(object);
            } else {
                throw new IllegalArgumentException(
                        "Field is not one of the acceptible types. Field: " + field);
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException("Could not convert field value to float", ex);
        }
    }

    public static void setFieldWithFloat(Object object, Field field, float value) {
        try {
            if (field.getType().equals(float.class)) {
                field.setFloat(object, value);
            } else if (field.getType().equals(int.class)) {
                field.setInt(object, Math.round(value));
            } else {
                throw new IllegalArgumentException("Field is not one of the acceptible types");
            }
        } catch (IllegalArgumentException | IllegalAccessException ex) {
            throw new RuntimeException("Could not set field value", ex);
        }
    }


    @SuppressWarnings("boxing")
    public static final Object stringToBoxed(Class<?> t, String strVal) {
        if (t.equals(boolean.class) || t.equals(Boolean.class)) {
            return Boolean.parseBoolean(strVal);
        } else if (t.equals(byte.class) || t.equals(Byte.class)) {
            return Byte.parseByte(strVal);
        } else if (t.equals(char.class) || t.equals(Character.class)) {
            return strVal.charAt(0);
        } else if (t.equals(double.class) || t.equals(Double.class)) {
            return Double.parseDouble(strVal);
        } else if (t.equals(float.class) || t.equals(Float.class)) {
            return Float.parseFloat(strVal);
        } else if (t.equals(int.class) || t.equals(Integer.class)) {
            return Integer.parseInt(strVal);
        } else if (t.equals(long.class) || t.equals(Long.class)) {
            return Long.parseLong(strVal);
        } else if (t.equals(short.class) || t.equals(Short.class)) {
            return Short.parseShort(strVal);
        } else if (t.equals(String.class)) {
            return strVal;
        }
        throw new IllegalArgumentException("Can't parse non-primitive type: " + t.getCanonicalName());
    }


    public static final boolean isPrimitive(Class<?> t) {
        return t.isPrimitive() ||
                t.equals(String.class) ||
                t.equals(Boolean.class) ||
                t.equals(Byte.class) ||
                t.equals(Character.class) ||
                t.equals(Double.class) ||
                t.equals(Float.class) ||
                t.equals(Integer.class) ||
                t.equals(Long.class) ||
                t.equals(Short.class) ||
                t.equals(String.class);
    }
}
