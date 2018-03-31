package cobweb3d.ui.config;

import cobwebutil.io.ConfXMLTag;

import java.lang.reflect.AnnotatedElement;

public abstract class PropertyAccessorBase implements PropertyAccessor {

    protected final PropertyAccessor parent;

    protected PropertyAccessorBase() {
        this(null);
    }

    protected PropertyAccessorBase(PropertyAccessor parent) {
        this.parent = parent;
    }

    @Override
    public int hashCode() {
        int code = 0;
        if (parent != null)
            code = parent.hashCode();

        code ^= thisHashCode();
        return code;
    }

    protected abstract int thisHashCode();

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PropertyAccessorBase) {
            PropertyAccessorBase o = (PropertyAccessorBase) obj;
            if (parent == null && o.parent == null)
                return true;
            if (parent != null && o.parent != null)
                return parent.equals(o.parent);
        }
        return false;
    }

    @Override
    public String getName() {
        String res = thisGetName();
        if (parent != null)
            res = parent.getName() + " " + res;

        return res;
    }

    protected abstract String thisGetName();

    @Override
    public String getIdentifier() {
        String identifier = getAnnotationSource().getAnnotation(ConfXMLTag.class).value();
        if (parent != null)
            identifier = parent.getIdentifier() + "." + identifier;

        return identifier;
    }

    @Override
    public String toString() {
        String res = thisToString();
        if (parent != null)
            res = parent.toString() + res;
        return res;
    }

    protected abstract String thisToString();

    @Override
    public Object get(Object object) {
        if (parent != null)
            object = parent.get(object);
        return thisGetValue(object);
    }

    protected abstract Object thisGetValue(Object object);

    @Override
    public float getAsFloat(Object object) {
        if (parent != null)
            object = parent.get(object);

        return thisGetValueFloat(object);
    }

    protected float thisGetValueFloat(Object object) {
        Object value = thisGetValue(object);
        if (value instanceof Float)
            return (float) value;
        else if (value instanceof Integer)
            return (int) value;
        else
            throw new IllegalArgumentException("Unable to convert property to float");
    }

    @Override
    public void set(Object object, Object value) {
        if (parent != null)
            object = parent.get(object);
        thisSetValue(object, value);
    }

    protected abstract void thisSetValue(Object object, Object value);

    @Override
    public void setAsFloat(Object object, float value) {
        if (parent != null)
            object = parent.get(object);
        thisSetValueFloat(object, value);
    }

    protected void thisSetValueFloat(Object object, float value) {
        Object val = value;
        if (getType().equals(int.class))
            val = (int) value;
        thisSetValue(object, val);
    }

    @Override
    public AnnotatedElement getAnnotationSource() {
        if (parent == null)
            throw new IllegalArgumentException("No annotation source!");
        else
            return parent.getAnnotationSource();
    }
}
