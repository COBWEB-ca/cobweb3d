package cobweb3d.ui.config;

import cobwebutil.io.ConfDisplayFormat;
import cobwebutil.io.ConfMap;

import java.util.Map;

/**
 * PropertyAccessor that gets/sets map values.
 */
public class MapPropertyAccessor extends PropertyAccessorBase {
    private final Object key;

    private final String format;

    /**
     * Creates accessor for value for given key and map returned by parent accessor.
     *
     * @param parent parent accessor that returns a map
     * @param key    key to access
     */
    public MapPropertyAccessor(PropertyAccessor parent, Object key) {
        super(parent);
        if (!Map.class.isAssignableFrom(parent.getType()))
            throw new IllegalArgumentException("Parent must return a map!");

        this.key = key;
        ConfDisplayFormat dispFormat = getAnnotationSource().getAnnotation(ConfDisplayFormat.class);
        this.format = dispFormat != null ? dispFormat.value() : null;
    }

    @Override
    protected int thisHashCode() {
        return key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof MapPropertyAccessor) {
            MapPropertyAccessor o = (MapPropertyAccessor) obj;
            return super.equals(o) && key.equals(o.key);
        }
        return false;
    }

    @Override
    public Object thisGetValue(Object obj) {
        @SuppressWarnings("unchecked")
        Map<Object, Object> map = (Map<Object, Object>) obj;
        return map.get(key);
    }

    @Override
    public void thisSetValue(Object obj, Object value) {
        @SuppressWarnings("unchecked")
        Map<Object, Object> map = (Map<Object, Object>) obj;
        map.put(key, value);
    }

    @Override
    public String getName() {
        return String.format(format, parent.getName(), key);
    }

    @Override
    protected String thisGetName() {
        return key.toString();
    }

    @Override
    public Class<?> getType() {
        return getAnnotationSource().getAnnotation(ConfMap.class).valueClass();
    }

    @Override
    public String thisToString() {
        return ".get(" + key.toString() + ")";
    }
}
