package cobweb3d.ui.config;

import cobwebutil.io.ConfDisplayName;

import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * PropertyAccessor that accesses getters/setters.
 */
public class SetterPropertyAccessor extends PropertyAccessorBase {

    private Method setter;
    private Method getter;

    public SetterPropertyAccessor(Method setter) {
        this(null, setter);
    }

    public SetterPropertyAccessor(PropertyAccessor parent, Method setter) {
        super(parent);
        this.setter = setter;
        String getterName = setter.getName().replaceFirst("^set", "get");
        try {
            this.getter = setter.getDeclaringClass().getMethod(getterName);
        } catch (NoSuchMethodException | SecurityException ex) {
            throw new IllegalArgumentException("Could not find matching getter for: " + setter.getName(), ex);
        }
    }

    @Override
    protected int thisHashCode() {
        return setter.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SetterPropertyAccessor) {
            SetterPropertyAccessor o = (SetterPropertyAccessor) obj;
            return super.equals(o) && setter.equals(o.setter);
        }
        return false;
    }

    @Override
    public Class<?> getType() {
        return setter.getParameterTypes()[0];
    }

    @Override
    public AnnotatedElement getAnnotationSource() {
        return setter;
    }

    @Override
    protected String thisGetName() {
        ConfDisplayName nameAnnotation = getAnnotationSource().getAnnotation(ConfDisplayName.class);
        String name = nameAnnotation != null ? nameAnnotation.value() : setter.getName();
        return name;
    }

    @Override
    protected String thisToString() {
        return "." + setter.getName();
    }

    @Override
    protected Object thisGetValue(Object object) {
        try {
            return getter.invoke(object);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void thisSetValue(Object object, Object value) {
        try {
            setter.invoke(object, value);
        } catch (IllegalAccessException | InvocationTargetException ex) {
            throw new RuntimeException(ex);
        }
    }

}
